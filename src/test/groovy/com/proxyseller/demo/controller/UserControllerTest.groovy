package com.proxyseller.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.UserRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    UserRepository userRepository

    @Autowired
    ObjectMapper objectMapper

    User user

    def setup() {
        user = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
    }

    def cleanup() {
        userRepository.deleteAll()
    }

    def "should get all users"() {
        given:
        userRepository.save(new User(name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password')))
        userRepository.save(new User(name: 'another_test_name0000',
                passwordHash: DigestUtils.sha256Hex('another_test_password')))

        when:
        def response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should find user by id"() {
        given:
        userRepository.save(user)

        when:
        def response = mockMvc.perform(get("/users/${user._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user._id))
                .andExpect(jsonPath('$[\'name\']').value(user.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(user.passwordHash))
    }

    def "should find user by name"() {
        given:
        userRepository.save(user)

        when:
        def response = mockMvc.perform(get("/users?name=${user.name}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user._id))
                .andExpect(jsonPath('$[\'name\']').value(user.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(user.passwordHash))
    }

    def "should add new user"() {
        given:
        def userEntryData = new UserEntryData('another_test_name', 'another_test_password')

        when:
        def response = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userEntryData))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').exists())
                .andExpect(jsonPath('$[\'name\']').value(userEntryData.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(userEntryData.passwordHash))
    }

    def "should edit existing user"() {
        given:
        def userEntryData = new UserEntryData('test_name', 'test_password')
        userRepository.save(user)

        when:
        def response = mockMvc.perform(put("/users/${user._id}")
                .content(objectMapper.writeValueAsString(userEntryData))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user._id))
                .andExpect(jsonPath('$[\'name\']').value(userEntryData.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(userEntryData.passwordHash))
    }

    def "should delete existing user"() {
        given:
        userRepository.save(user)

        when:
        mockMvc.perform(delete("/users/${user._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/users/${user._id}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
    }

    class UserEntryData {
        String name
        String password
        String passwordHash

        UserEntryData(String name, String password) {
            this.name = name
            this.password = password
            this.passwordHash = DigestUtils.sha256Hex(password)
        }
    }
}
