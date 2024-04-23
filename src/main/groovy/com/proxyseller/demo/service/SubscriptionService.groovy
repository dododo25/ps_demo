package com.proxyseller.demo.service

import com.proxyseller.demo.model.Subscription
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.SubscriptionRepository
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
        return repository.findAllSubscriptionsByUserId(id)
                .stream()
                .map { it.targetUser }
                .toList()
    }

    List<User> findAllSubscribersByUserId(String id) {
        return repository.findAllSubscribersByUserId(id)
                .stream()
                .map { it.subscriber }
                .toList()
    }

    Subscription save(Subscription entity) {
        if (!entity.targetUser || !entity.subscriber) {
            throw new NullPointerException()
        }
        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
