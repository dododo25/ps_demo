package com.proxyseller.demo.repository

import com.proxyseller.demo.model.PostUpVote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostUpVoteRepository extends MongoRepository<PostUpVote, String> {

    @Query(value = "{'user.id': ?0}")
    List<PostUpVote> findAllByUserId(String userId)
}
