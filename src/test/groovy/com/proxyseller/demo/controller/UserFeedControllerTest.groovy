package com.proxyseller.demo.controller

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
class UserFeedControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    PostUpVoteRepository postUpVoteRepository

    @Autowired
    CommentRepository commentRepository

    @Autowired
    PostRepository postRepository

    @Autowired
    UserRepository userRepository

    def cleanup() {
        postUpVoteRepository.deleteAll()
        commentRepository.deleteAll()
        postRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "should get user`s feed by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        def post1 = new Post(_id: ObjectId.get().toHexString(), author: user1,
                content: 'test_post_content', creationDate: new Date(10000))
        def post2 = new Post(_id: ObjectId.get().toHexString(), author: user2,
                content: 'another_test_post_content', creationDate: new Date(10000))
        def comment = new Comment(_id: ObjectId.get().toHexString(), commenter: user1, post: post2,
                content: 'test_comment_content', creationDate: new Date(20000))
        def upVote = new PostUpVote(_id: ObjectId.get().toHexString(), user: user1,
                post: post2, creationDate: new Date(15000))

        userRepository.save(user1)
        userRepository.save(user2)
        postRepository.save(post1)
        postRepository.save(post2)
        commentRepository.save(comment)
        postUpVoteRepository.save(upVote)

        when:
        def response = mockMvc.perform(get("/users/${user1._id}/feed")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(3))
                .andExpect(jsonPath('$[0][\'type\']').value('comment'))
                .andExpect(jsonPath('$[0][\'entity\'][\'_id\']').value(comment._id))
                .andExpect(jsonPath('$[1][\'type\']').value('upvote'))
                .andExpect(jsonPath('$[2][\'type\']').value('post'))
                .andExpect(jsonPath('$[2][\'entity\'][\'_id\']').value(post1._id))
    }
}
