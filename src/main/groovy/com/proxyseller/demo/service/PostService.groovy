package com.proxyseller.demo.service

import com.proxyseller.demo.exception.EntityNullPointerException
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {

    @Autowired
    private PostRepository repository

    List<Post> findAll() {
        return repository.findAll()
    }

    List<Post> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId)
    }

    Optional<Post> findById(String id) {
        return repository.findById(id)
    }

    Post save(Post entity) {
        if (!entity.author) {
            throw new EntityNullPointerException('null post author value')
        }

        if (!entity.content) {
            throw new EntityNullPointerException('null post content value')
        }

        if (!entity.creationDate) {
            throw new NullPointerException()
        }

        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
