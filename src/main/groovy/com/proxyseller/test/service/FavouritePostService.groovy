package com.proxyseller.test.service

import com.proxyseller.test.model.FavouritePost
import com.proxyseller.test.model.Post
import com.proxyseller.test.repository.FavouritePostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FavouritePostService {

    @Autowired
    private FavouritePostRepository repository

    List<FavouritePost> findAll() {
        return repository.findAll()
    }

    List<Post> findAllPostsByUserId(long id) {
        return repository.findAllByUserId(id)
    }

    FavouritePost save(FavouritePost entity) {
        return repository.save(entity)
    }

    void delete(long userId, long postId) {
        repository.delete(userId, postId)
    }
}
