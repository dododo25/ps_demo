package com.proxyseller.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.PostRepository
import com.proxyseller.demo.repository.UserRepository
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    ObjectMapper objectMapper

    User user

    Post post

    def setup() {
        user = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        post = new Post(_id: ObjectId.get().toHexString(), author: user, content: 'test_post_content')
    }

    def cleanup() {
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should get all posts"() {
        given:
        def user1 = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))

        def post1 = new Post(author: user1, content: 'test_post_content')
        def post2 = new Post(author: user2, content: 'another_test_post_content')

        postRepository.save(post1)
        postRepository.save(post2)

        when:
        def response = mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should get post by id"() {
        given:
        postRepository.save(post)

        when:
        def response = mockMvc.perform(get("/posts/${post._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(post._id))
                .andExpect(jsonPath('$[\'author\'][\'_id\']').value(user._id))
                .andExpect(jsonPath('$[\'content\']').value(post.content))
    }

    def "should get all posts by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def post1 = new Post(author: user1, content: 'test_post_content')
        def post2 = new Post(author: user1, content: 'another_test_post_content')

        postRepository.save(post1)
        postRepository.save(post2)

        when:
        def response = mockMvc.perform(get("/users/${user1._id}/posts")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should add new post"() {
        given:
        post.author = userRepository.save(user)

        when:
        def response = mockMvc.perform(post("/users/${post.author._id}/posts")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').exists())
                .andExpect(jsonPath('$[\'author\'][\'_id\']').value(post.author._id))
                .andExpect(jsonPath('$[\'content\']').value(post.content))
    }

    def "should edit existing post"() {
        given:
        def anotherPost = new Post(_id: post._id, author: user, content: 'another_test_post_content')

        when:
        def response = mockMvc.perform(put("/posts/${post._id}")
                .content(objectMapper.writeValueAsString(anotherPost))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(anotherPost._id))
                .andExpect(jsonPath('$[\'author\'][\'_id\']').value(anotherPost.author._id))
                .andExpect(jsonPath('$[\'content\']').value(anotherPost.content))
    }

    def "should delete existing post"() {
        given:
        postRepository.save(post)

        when:
        mockMvc.perform(delete("/posts/${post._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/posts/${post._id}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
    }
}
