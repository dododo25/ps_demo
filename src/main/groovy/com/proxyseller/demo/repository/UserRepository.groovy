package com.proxyseller.demo.repository

import com.proxyseller.demo.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String> {

    @Query("{'name': ?0}")
    Optional<User> findByName(String name)
}
