package com.proxyseller.demo.service

import com.proxyseller.demo.model.FavouritePost
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.repository.FavouritePostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FavouritePostService {

    @Autowired
    private FavouritePostRepository repository

    List<FavouritePost> findAll() {
        return repository.findAll()
    }

    List<Post> findAllPostsByUserId(String id) {
        return repository.findAllByUserId(id)
                .stream()
                .map { it.post }
                .toList()
    }

    FavouritePost save(FavouritePost entity) {
        return repository.save(entity)
    }

    void delete(FavouritePost entity) {
        repository.delete(entity)
    }
}
