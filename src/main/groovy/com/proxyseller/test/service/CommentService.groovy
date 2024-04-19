package com.proxyseller.test.service

import com.proxyseller.test.model.Comment
import com.proxyseller.test.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    private CommentRepository repository

    List<Comment> findAll() {
        return repository.findAll()
    }

    List<Comment> findAllByPostId(String id) {
        return repository.findAllByPostId(id)
    }

    List<Comment> findAllByUserId(String id) {
        return repository.findAllByUserId(id)
    }

    Optional<Comment> findById(String id) {
        return repository.findById(id)
    }

    Comment save(Comment entity) {
        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
