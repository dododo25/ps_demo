package com.proxyseller.test.repository

import com.proxyseller.test.model.FavouritePost
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FavouritePostRepository extends MongoRepository<FavouritePost, String> {

    @Query(value = "{'user.id': ?0}")
    List<FavouritePost> findAllByUserId(String userId)
}
