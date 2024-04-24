package com.proxyseller.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.demo.model.Comment
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.PostUpVote
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.CommentRepository
import com.proxyseller.demo.repository.PostRepository
import com.proxyseller.demo.repository.PostUpVoteRepository
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
class PostUpVoteControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    PostUpVoteRepository upVoteRepository

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    ObjectMapper objectMapper

    User user

    Post post

    PostUpVote upVote

    def setup() {
        user = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        post = new Post(_id: ObjectId.get().toHexString(), author: user, content: 'test_post_content')
        upVote = new PostUpVote(_id: ObjectId.get().toHexString(), user: user, post: post)
    }

    def cleanup() {
        upVoteRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should get all upvotes by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))

        def post1 = new Post(author: user1, content: 'test_post_content')
        def post2 = new Post(author: user1, content: 'another_test_post_content')

        def upVote1 = new PostUpVote(_id: ObjectId.get().toHexString(), post: post1, user: user2)
        def upVote2 = new PostUpVote(_id: ObjectId.get().toHexString(), post: post2, user: user2)

        upVoteRepository.save(upVote1)
        upVoteRepository.save(upVote2)

        when:
        def response = mockMvc.perform(get("/users/${user2._id}/favourites")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "add upvote by post id and user id"() {
        given:
        upVote.user = userRepository.save(user)
        upVote.post = postRepository.save(post)

        when:
        def response = mockMvc.perform(post("/users/${upVote.user._id}" +
                "/favourites?post=${upVote.post._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').exists())
                .andExpect(jsonPath('$[\'user\'][\'_id\']').value(upVote.user._id))
                .andExpect(jsonPath('$[\'post\'][\'_id\']').value(upVote.post._id))
    }

    def "delete upvote by post id and user id"() {
        given:
        userRepository.save(user)
        postRepository.save(post)
        upVoteRepository.save(upVote)

        when:
        mockMvc.perform(delete("/users/${upVote.user._id}" +
                "/favourites?post=${upVote.post._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/users/${upVote.user._id}/favourites")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(0))
    }
}
