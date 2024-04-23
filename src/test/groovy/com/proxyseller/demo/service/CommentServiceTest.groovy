package com.proxyseller.demo.service

import com.proxyseller.demo.model.Comment
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.CommentRepository
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class CommentServiceTest extends Specification {

    @Autowired
    @Subject
    CommentService commentService

    @Autowired
    CommentRepository commentRepository

    User user

    Post post

    def setup() {
        user = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        post = new Post(_id: ObjectId.get().toHexString(), author: user, content: 'test_post_content')
    }

    def cleanup() {
        commentRepository.deleteAll()
    }

    def "should save valid comment"() {
        given:
        def validComment = new Comment(post: post, commenter: user, content: 'test_comment_content')

        when:
        def savedComment = commentService.save(validComment)

        then:
        savedComment._id != null
    }

    def "should throw exception when comment post is invalid"() {
        given:
        def invalidComment = new Comment(commenter: user, content: 'test_comment_content')

        when:
        commentService.save(invalidComment)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when comment commenter is invalid"() {
        given:
        def invalidComment = new Comment(post: post, content: 'test_comment_content')

        when:
        commentService.save(invalidComment)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when comment content is invalid"() {
        given:
        def invalidComment = new Comment(post: post, commenter: user)

        when:
        commentService.save(invalidComment)

        then:
        thrown(NullPointerException)
    }

    def "should find all comments"() {
        when:
        def comments = commentService.findAll()

        then:
        comments != null
    }

    def "should find post by id"() {
        given:
        def comment = new Comment(post: post, commenter: user, content: 'test_comment_content')
        def savedComment = commentService.save(comment)

        when:
        def commentToFound = commentService.findById(savedComment._id)

        then:
        savedComment._id == commentToFound.map { it._id }.orElse(null)
    }

    def "should find all comments by post id"() {
        given:
        def comment1 = new Comment(post: post, commenter: user, content: 'test_comment_content')
        def comment2 = new Comment(post: post, commenter: user, content: 'another_test_comment_content')

        def savedComment1 = commentService.save(comment1)
        def savedComment2 = commentService.save(comment2)

        when:
        def posts = commentService.findAllByPostId(post._id)

        then:
        posts.stream().map { it._id }.anyMatch { it == savedComment1._id }
        posts.stream().map { it._id }.anyMatch { it == savedComment2._id }
    }

    def "should find all comments by user id"() {
        given:
        def comment1 = new Comment(post: post, commenter: user, content: 'test_comment_content')
        def comment2 = new Comment(post: post, commenter: user, content: 'another_test_comment_content')

        def savedComment1 = commentService.save(comment1)
        def savedComment2 = commentService.save(comment2)

        when:
        def posts = commentService.findAllByUserId(user._id)

        then:
        posts.stream().map { it._id }.anyMatch { it == savedComment1._id }
        posts.stream().map { it._id }.anyMatch { it == savedComment2._id }
    }

    def "should delete comment by id"() {
        given:
        def comment = new Comment(post: post, commenter: user, content: 'test_comment_content')
        def savedComment = commentService.save(comment)

        when:
        commentService.deleteById(savedComment._id)

        then:
!        commentService.findById(savedComment._id).isPresent()
    }
}
