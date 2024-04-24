package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Subscription
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.SubscriptionRepository
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
class SubscriptionControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    SubscriptionRepository subscriptionRepository

    @Autowired
    UserRepository userRepository

    User targetUser

    User subscriber

    Subscription subscription

    def setup() {
        targetUser = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        subscriber = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        subscription = new Subscription(_id: ObjectId.get().toHexString(),
                targetUser: targetUser, subscriber: subscriber)
    }

    def cleanup() {
        userRepository.deleteAll()
        subscriptionRepository.deleteAll()
    }

    def "should get all subscriptions by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        def user3 = new User(_id: ObjectId.get().toHexString(), name: 'yet_another_test_name',
                passwordHash: DigestUtils.sha256Hex('yet_another_test_password'))

        def sub1 = new Subscription(targetUser: user1, subscriber: user3)
        def sub2 = new Subscription(targetUser: user2, subscriber: user3)

        subscriptionRepository.save(sub1)
        subscriptionRepository.save(sub2)

        when:
        def response = mockMvc.perform(get("/users/${user3._id}/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "should get all subscribers by user id"() {
        given:
        def user1 = new User(_id: ObjectId.get().toHexString(), name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))
        def user2 = new User(_id: ObjectId.get().toHexString(), name: 'another_test_name',
                passwordHash: DigestUtils.sha256Hex('another_test_password'))
        def user3 = new User(_id: ObjectId.get().toHexString(), name: 'yet_another_test_name',
                passwordHash: DigestUtils.sha256Hex('yet_another_test_password'))

        def sub1 = new Subscription(targetUser: user1, subscriber: user2)
        def sub2 = new Subscription(targetUser: user1, subscriber: user3)

        subscriptionRepository.save(sub1)
        subscriptionRepository.save(sub2)

        when:
        def response = mockMvc.perform(get("/users/${user1._id}/subscribers")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def "add new subscription by user ids"() {
        given:
        subscription.targetUser = userRepository.save(targetUser)
        subscription.subscriber = userRepository.save(subscriber)

        when:
        mockMvc.perform(post("/users/${subscriber._id}/subscriptions?target=${targetUser._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/users/${subscription.subscriber._id}/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0][\'_id\']').value(subscription.targetUser._id))
    }

    def "delete upvote by post id and user id"() {
        given:
        userRepository.save(targetUser)
        userRepository.save(subscriber)
        subscriptionRepository.save(subscription)

        when:
        mockMvc.perform(delete("/users/${subscriber._id}/subscriptions?target=${targetUser._id}")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        mockMvc.perform(get("/users/${subscriber._id}/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(0))
    }
}
