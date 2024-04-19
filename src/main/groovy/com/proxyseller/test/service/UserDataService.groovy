package com.proxyseller.test.service

import com.proxyseller.test.model.UserData
import com.proxyseller.test.repository.UserDataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserDataService {

    @Autowired
    private UserDataRepository repository

    List<UserData> findAll() {
        return repository.findAll()
    }

    Optional<UserData> findById(long id) {
        return repository.findById(id)
    }

    UserData save(UserData entity) {
        return repository.save(entity)
    }

    void deleteById(long id) {
        repository.deleteById(id)
    }
}
