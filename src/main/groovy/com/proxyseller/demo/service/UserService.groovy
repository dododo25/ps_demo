package com.proxyseller.demo.service

import com.proxyseller.demo.exception.EntityNullPointerException
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private UserRepository repository

    List<User> findAll() {
        return repository.findAll()
    }

    Optional<User> findById(String id) {
        return repository.findById(id)
    }

    Optional<User> findByName(String name) {
        return repository.findByName(name)
    }

    User save(User entity) {
        if (!entity.name) {
            throw new EntityNullPointerException('null user name value')
        }

        if (!entity.passwordHash) {
            throw new EntityNullPointerException('null user password value')
        }

        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
