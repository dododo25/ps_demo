package com.proxyseller.demo.repository

import com.proxyseller.demo.model.Subscription
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    @Query("{'subscriber._id': ?0}")
    List<Subscription> findAllSubscriptionsBySubscriberId(String id)

    @Query("{'targetUser._id': ?0}")
    List<Subscription> findAllSubscriptionsByTargetUserId(String id)

    @Query("{'targetUser._id': ?0, 'subscriber._id': ?1}")
    Optional<Subscription> findByTargetUserIdAndSubscriberId(String userId, String subscriberId)
}
