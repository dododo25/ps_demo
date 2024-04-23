package com.proxyseller.demo.repository

import com.proxyseller.demo.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {

    @Query("{'author._id': ?0}")
    List<Post> findAllByUserId(String userId)
}
