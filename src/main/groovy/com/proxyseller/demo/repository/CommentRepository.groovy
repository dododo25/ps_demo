package com.proxyseller.demo.repository

import com.proxyseller.demo.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{'post._id': ?0}")
    List<Comment> findAllByPostId(String id)

    @Query("{'commenter._id': ?0}")
    List<Comment> findAllByUserId(String id)
}
