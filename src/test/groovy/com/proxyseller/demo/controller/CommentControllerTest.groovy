package com.proxyseller.demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.proxyseller.demo.model.Comment
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.CommentRepository
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
class CommentControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    CommentRepository commentRepository

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    ObjectMapper objectMapper

    User user

    Post post

    Comment comment

    def setup() {
        user = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        post = new Post(_id: ObjectId.get().toHexString(), author: user, content: 'test_post_content')
        comment = new Comment(_id: ObjectId.get().toHexString(), commenter: user, post: post,
                content: 'test_comment_content')
    }

    def cleanup() {
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should get all comments"() {
        given:
        def user1 = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        def user3 = new User(name: 'yet_another_test_name',
                passwordHash: DigestUtils.sha256Hex('yet_another_test_password'))

        def post = new Post(author: user1, content: 'test_post_content')

        def comment1 = new Comment(commenter: user2, post: post, content: 'test_comment_content')
        def comment2 = new Comment(commenter: user3, post: post, content: 'test_comment_content')

        commentRepository.save(comment1)
        commentRepository.save(comment2)

        when:
        def response = mockMvc.perform(get("/comments")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should get comment by id"() {
        given:
        commentRepository.save(comment)

        when:
        def response = mockMvc.perform(get("/comments/${comment._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(comment._id))
                .andExpect(jsonPath('$[\'post\'][\'_id\']').value(post._id))
                .andExpect(jsonPath('$[\'commenter\'][\'_id\']').value(user._id))
                .andExpect(jsonPath('$[\'content\']').value(comment.content))
    }

    def "should get all comments by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))

        def post1 = new Post(author: user1, content: 'test_post_content')
        def post2 = new Post(author: user1, content: 'another_test_post_content')

        def comment1 = new Comment(commenter: user2, post: post1, content: 'test_comment_content')
        def comment2 = new Comment(commenter: user2, post: post2, content: 'another_test_comment_content')

        commentRepository.save(comment1)
        commentRepository.save(comment2)

        when:
        def response = mockMvc.perform(get("/users/${user2._id}/comments")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should get all comments by post id"() {
        given:
        def user1 = new User(name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        def user3 = new User(name: 'yet_another_test_name',
                passwordHash: DigestUtils.sha256Hex('yet_another_test_password'))

        def post = new Post(_id: ObjectId.get().toHexString(), author: user1, content: 'test_post_content')

        def comment1 = new Comment(commenter: user2, post: post, content: 'test_comment_content')
        def comment2 = new Comment(commenter: user3, post: post, content: 'another_test_comment_content')

        commentRepository.save(comment1)
        commentRepository.save(comment2)

        when:
        def response = mockMvc.perform(get("/posts/${post._id}/comments")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should add new comment"() {
        given:
        comment.commenter = userRepository.save(user)
        comment.post = postRepository.save(post)

        when:
        def response = mockMvc.perform(post("/posts/${comment.post._id}" +
                "/comments?user=${comment.commenter._id}")
                .content(objectMapper.writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').exists())
                .andExpect(jsonPath('$[\'commenter\'][\'_id\']').value(comment.commenter._id))
                .andExpect(jsonPath('$[\'post\'][\'_id\']').value(comment.post._id))
                .andExpect(jsonPath('$[\'content\']').value(comment.content))
    }

    def "should edit existing comment"() {
        given:
        def anotherComment = new Comment(_id: comment._id, commenter: user, post: post,
                content: 'another_test_post_content', creationDate: new Date())

        when:
        def response = mockMvc.perform(put("/comments/${comment._id}")
                .content(objectMapper.writeValueAsString(anotherComment))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$[\'_id\']').value(anotherComment._id))
                .andExpect(jsonPath('$[\'commenter\'][\'_id\']').value(anotherComment.commenter._id))
                .andExpect(jsonPath('$[\'post\'][\'_id\']').value(anotherComment.post._id))
                .andExpect(jsonPath('$[\'content\']').value(anotherComment.content))
    }

    def "should delete existing comment"() {
        given:
        commentRepository.save(comment)

        when:
        mockMvc.perform(delete("/comments/${comment._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/comment/${comment._id}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
    }
}
