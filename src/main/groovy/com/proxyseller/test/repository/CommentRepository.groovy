package com.proxyseller.test.repository

import com.proxyseller.test.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{'post.id': ?0}")
    List<Comment> findAllByPostId(String id)

    @Query("{'commenter.id': ?0}")
    List<Comment> findAllByUserId(String id)
}
