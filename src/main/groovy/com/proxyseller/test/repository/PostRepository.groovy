package com.proxyseller.test.repository

import com.proxyseller.test.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {

    @Query("{'author.id': ?0}")
    List<Post> findAllByUserId(String userId)
}
