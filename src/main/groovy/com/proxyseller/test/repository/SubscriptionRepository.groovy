package com.proxyseller.test.repository

import com.proxyseller.test.model.Subscription
import com.proxyseller.test.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    @Query("{'subscriber.id': ?0}")
    List<Subscription> findAllByUserId(String id)
}
