package com.proxyseller.demo.service

import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.PostUpVote
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.PostUpVoteRepository
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class PostUpVoteServiceTest extends Specification {

    @Autowired
    @Subject
    PostUpVoteService upvoteService

    @Autowired
    PostUpVoteRepository upvoteRepository

    User user1

    User user2

    Post post1

    Post post2

    def setup() {
        user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        post1 = new Post(_id: ObjectId.get().toHexString(), author: user1, content: 'test_post_content')
        post2 = new Post(_id: ObjectId.get().toHexString(), author: user1, content: 'another_test_post_content')
    }

    def cleanup() {
        upvoteRepository.deleteAll()
    }

    def "should save valid upvote"() {
        given:
        def validUpVote = new PostUpVote(user: user1, post: post1)

        when:
        def savedUpVote = upvoteService.save(validUpVote)

        then:
        savedUpVote._id != null
    }

    def "should throw exception when upvote user is invalid"() {
        given:
        def invalidUpVote = new PostUpVote(post: post1)

        when:
        upvoteService.save(invalidUpVote)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when upvote post is invalid"() {
        given:
        def invalidUpVote = new PostUpVote(user: user1)

        when:
        upvoteService.save(invalidUpVote)

        then:
        thrown(NullPointerException)
    }

    def "should find all upvotes"() {
        when:
        def upvoteList = upvoteService.findAll()

        then:
        upvoteList != null
    }

    def "should find all upvoted posts by user id"() {
        given:
        def upVote1 = new PostUpVote(user: user1, post: post1)
        def upVote2 = new PostUpVote(user: user1, post: post2)

        upvoteService.save(upVote1)
        upvoteService.save(upVote2)

        when:
        def upVotes = upvoteService.findAllPostsByUserId(user1._id)

        then:
        upVotes.stream().map { it._id }.anyMatch { it == post1._id }
        upVotes.stream().map { it._id }.anyMatch { it == post2._id }
    }

    def "should find all upvote users by post id"() {
        given:
        def upVote1 = new PostUpVote(user: user1, post: post1)
        def upVote2 = new PostUpVote(user: user2, post: post1)

        upvoteService.save(upVote1)
        upvoteService.save(upVote2)

        when:
        def upVotes = upvoteService.findAllUsersByPostId(post1._id)

        then:
        upVotes.stream().map { it._id }.anyMatch { it == user1._id }
        upVotes.stream().map { it._id }.anyMatch { it == user2._id }
    }

    def "should delete upvote by id"() {
        given:
        def upVote = new PostUpVote(user: user1, post: post1)
        def savedUpVote = upvoteService.save(upVote)

        when:
        upvoteService.deleteById(savedUpVote._id)

        then:
        upvoteService.findAll().stream().noneMatch { it._id == savedUpVote._id }
    }
}
