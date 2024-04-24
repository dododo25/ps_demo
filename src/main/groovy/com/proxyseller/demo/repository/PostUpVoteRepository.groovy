package com.proxyseller.demo.repository

import com.proxyseller.demo.model.PostUpVote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostUpVoteRepository extends MongoRepository<PostUpVote, String> {

    @Query(value = "{'user._id': ?0}")
    List<PostUpVote> findAllByUserId(String userId)

    @Query(value = "{'post._id': ?0}")
    List<PostUpVote> findAllByPostId(String postId)

    @Query(value = "{'post._id': ?0, 'user._id': ?1}")
    Optional<PostUpVote> findByPostIdAndUserId(String postId, String userId)
}
