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

    User user1

    User user2

    UserEntryData entry1

    UserEntryData entry2

    def setup() {
        user1 = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
        user2 = new User(name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))

        entry1 = new UserEntryData('test_name', 'test_password')
        entry2 = new UserEntryData('another_test_name', 'another_test_password')
    }

    def cleanup() {
        userRepository.deleteAll()
    }

    def "should get all users"() {
        given:
        userRepository.save(user1)
        userRepository.save(user2)

        when:
        def response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should find user by id"() {
        given:
        userRepository.save(user1)

        when:
        def response = mockMvc.perform(get("/users/${user1._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user1._id))
                .andExpect(jsonPath('$[\'name\']').value(user1.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(user1.passwordHash))
    }

    def "should get 4xx error when find user by nonexistent id"() {
        when:
        def response = mockMvc.perform(get("/users/${user1._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should find user by name"() {
        given:
        userRepository.save(user1)

        when:
        def response = mockMvc.perform(get("/users?name=${user1.name}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user1._id))
                .andExpect(jsonPath('$[\'name\']').value(user1.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(user1.passwordHash))
    }

    def "should get 4xx error when find user by nonexistent name"() {
        when:
        def response = mockMvc.perform(get("/users?name=${user1.name}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should add new user"() {
        when:
        def response = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(entry1))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').exists())
                .andExpect(jsonPath('$[\'name\']').value(entry1.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(entry1.passwordHash))
    }

    def "should throw 4xx error when add new user with name as null"() {
        given:
        def userEntryData = new UserEntryData(null, 'another_test_password')

        when:
        def response = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userEntryData))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should throw 4xx error when add new user with password as null"() {
        given:
        def userEntryData = new UserEntryData('another_test_name', null)

        when:
        def response = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userEntryData))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should throw 4xx error when add new user with existing name"() {
        given:
        def userEntryData = new UserEntryData('test_name', 'any_password')
        userRepository.save(user1)

        when:
        def response = mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userEntryData))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should update existing user"() {
        given:
        user1 = userRepository.save(user1)

        when:
        def response = mockMvc.perform(put("/users/${user1._id}")
                .content(objectMapper.writeValueAsString(entry2))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(user1._id))
                .andExpect(jsonPath('$[\'name\']').value(entry2.name))
                .andExpect(jsonPath('$[\'passwordHash\']').value(entry2.passwordHash))
    }

    def "should throw 4xx error when update existing user with name as null"() {
        given:
        userRepository.save(user1)

        when:
        def response = mockMvc.perform(put("/users/${user1._id}")
                .content(objectMapper.writeValueAsString(new UserEntryData(null, 'any_password')))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should throw 4xx error when update existing user with password as null"() {
        given:
        userRepository.save(user1)

        when:
        def response = mockMvc.perform(put("/users/${user1._id}")
                .content(objectMapper.writeValueAsString(new UserEntryData('any_name', null)))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should throw 4xx error when update existing user with existing name"() {
        given:
        userRepository.save(user1)
        userRepository.save(user2)

        when:
        def response = mockMvc.perform(put("/users/${user1._id}")
                .content(objectMapper.writeValueAsString(entry2))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "should delete existing user"() {
        given:
        userRepository.save(user1)

        when:
        mockMvc.perform(delete("/users/${user1._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/users/${user1._id}")
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
            this.passwordHash = password ? DigestUtils.sha256Hex(password) : null
        }
    }
}
