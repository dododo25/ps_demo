package com.proxyseller.demo.repository

import com.proxyseller.demo.model.Subscription
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    @Query("{'subscriber.id': ?0}")
    List<Subscription> findAllByUserId(String id)
}
