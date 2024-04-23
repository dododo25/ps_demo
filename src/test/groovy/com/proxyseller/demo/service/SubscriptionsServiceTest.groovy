package com.proxyseller.demo.service

import com.proxyseller.demo.model.Subscription
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.SubscriptionRepository
import org.apache.commons.codec.digest.DigestUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class SubscriptionsServiceTest extends Specification {

    @Autowired
    @Subject
    SubscriptionService subscriptionService

    @Autowired
    SubscriptionRepository subscriptionRepository

    User user1

    User user2

    User user3

    def setup() {
        user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        user3 = new User(_id: ObjectId.get().toHexString(), name: 'yet_another_test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
    }

    def cleanup() {
        subscriptionRepository.deleteAll()
    }

    def "should save valid subscription"() {
        given:
        def validSubscription = new Subscription(targetUser: user1, subscriber: user2)

        when:
        def savedSubscription = subscriptionService.save(validSubscription)

        then:
        savedSubscription._id != null
    }

    def "should throw exception when subscription target user is invalid"() {
        given:
        def invalidSubscription = new Subscription(targetUser: user1)

        when:
        subscriptionService.save(invalidSubscription)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when subscription subscriber is invalid"() {
        given:
        def invalidSubscription = new Subscription(subscriber: user2)

        when:
        subscriptionService.save(invalidSubscription)

        then:
        thrown(NullPointerException)
    }

    def "should find all subscriptions"() {
        when:
        def upvoteList = subscriptionService.findAll()

        then:
        upvoteList != null
    }

    def "should find all subscription subscribers by user id"() {
        given:
        def subscription1 = new Subscription(targetUser: user1, subscriber: user2)
        def subscription2 = new Subscription(targetUser: user1, subscriber: user3)

        subscriptionService.save(subscription1)
        subscriptionService.save(subscription2)

        when:
        def subscribers = subscriptionService.findAllSubscribersByUserId(user1._id)

        then:
        subscribers.stream().map { it._id }.anyMatch { it == user2._id }
        subscribers.stream().map { it._id }.anyMatch { it == user3._id }
    }

    def "should find all subscribed users by user id"() {
        given:
        def subscription1 = new Subscription(targetUser: user1, subscriber: user2)
        subscriptionService.save(subscription1)

        when:
        def upVotes = subscriptionService.findAllSubscriptionsByUserId(user2._id)

        then:
        upVotes.stream().map { it._id }.anyMatch { it == user1._id }
    }

    def "should delete upvote by id"() {
        given:
        def subscription = new Subscription(targetUser: user1, subscriber: user2)
        def savedSubscription = subscriptionService.save(subscription)

        when:
        subscriptionService.deleteById(savedSubscription._id)

        then:
        subscriptionService.findAll().stream().noneMatch { it._id == savedSubscription._id }
    }
}
