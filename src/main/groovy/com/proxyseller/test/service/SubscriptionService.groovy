package com.proxyseller.test.service

import com.proxyseller.test.model.Subscription
import com.proxyseller.test.model.User
import com.proxyseller.test.repository.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SubscriptionService {

    @Autowired
    private SubscriptionRepository repository

    List<Subscription> findAll() {
        return repository.findAll()
    }

    List<User> findAllSubscriptionsByUserId(String id) {
        return repository.findAllByUserId(id)
                .stream()
                .map { it.subscriber }
                .toList()
    }

    Subscription save(Subscription entity) {
        return repository.save(entity)
    }

    void delete(Subscription entity) {
        repository.delete(entity)
    }
}
