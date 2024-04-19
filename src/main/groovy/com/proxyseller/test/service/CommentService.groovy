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

    List<Comment> findAllByPostId(long id) {
        return repository.findAllByPostId(id)
    }

    List<Comment> findAllByUserId(long id) {
        return repository.findAllByUserId(id)
    }

    Optional<Comment> findById(long id) {
        return repository.findById(id)
    }

    Comment save(Comment entity) {
        return repository.save(entity)
    }

    void deleteById(long id) {
        repository.deleteById(id)
    }
}
