package com.proxyseller.demo.service

import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.PostRepository
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class PostServiceTest extends Specification {

    @Autowired
    @Subject
    PostService postService

    @Autowired
    PostRepository postRepository

    User user

    def setup() {
        user = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
    }

    def cleanup() {
        postRepository.deleteAll()
    }

    def "should save valid post"() {
        given:
        def validPost = new Post(author: user, content: 'test_post_content', creationDate: new Date())

        when:
        def savedPost = postService.save(validPost)

        then:
        savedPost._id != null
    }

    def "should throw exception when post author is invalid"() {
        given:
        def invalidPost = new Post(content: 'test_post_content')

        when:
        postService.save(invalidPost)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when post content is invalid"() {
        given:
        def invalidPost = new Post(author: user)

        when:
        postService.save(invalidPost)

        then:
        thrown(NullPointerException)
    }

    def "should find all posts"() {
        when:
        def posts = postService.findAll()

        then:
        posts != null
    }

    def "should find post by id"() {
        given:
        def post = new Post(author: user, content: 'test_post_content', creationDate: new Date())
        def savedPost = postService.save(post)

        when:
        def postToFound = postService.findById(savedPost._id)

        then:
        savedPost._id == postToFound.map { it._id }.orElse(null)
    }

    def "should find all posts by user id"() {
        given:
        def post1 = new Post(author: user, content: 'test_post_content', creationDate: new Date())
        def post2 = new Post(author: user, content: 'another_test_post_content', creationDate: new Date())
        def savedPost1 = postService.save(post1)
        def savedPost2 = postService.save(post2)

        when:
        def posts = postService.findAllByUserId(user._id)

        then:
        posts.stream().map { it._id }.anyMatch { it == savedPost1._id }
        posts.stream().map { it._id }.anyMatch { it == savedPost2._id }
    }

    def "should delete post by id"() {
        given:
        def post = new Post(author: user, content: 'test_post_content', creationDate: new Date())
        def savedPost = postService.save(post)

        when:
        postService.deleteById(savedPost._id)

        then:
!        postService.findById(savedPost._id).isPresent()
    }
}
