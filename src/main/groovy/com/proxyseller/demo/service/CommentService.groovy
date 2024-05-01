package com.proxyseller.demo.service

import com.proxyseller.demo.exception.EntityNullPointerException
import com.proxyseller.demo.model.Comment
import com.proxyseller.demo.repository.CommentRepository
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
        if (!entity.post) {
            throw new EntityNullPointerException('null comment post value')
        }

        if (!entity.commenter) {
            throw new EntityNullPointerException('null comment commenter value')
        }

        if (!entity.content) {
            throw new EntityNullPointerException('null comment content value')
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
