package com.proxyseller.demo.service

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

    User save(User entity) {
        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
