package com.proxyseller.test.service

import com.proxyseller.test.model.Post
import com.proxyseller.test.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {

    @Autowired
    private PostRepository repository

    List<Post> findAll() {
        return repository.findAll()
    }

    List<Post> findAllByUserId(long id) {
        return repository.findAllByUserId(id)
    }

    Optional<Post> findById(long id) {
        return repository.findById(id)
    }

    Post save(Post entity) {
        return repository.save(entity)
    }

    void deleteById(long id) {
        repository.deleteById(id)
    }
}
