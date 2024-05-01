package com.proxyseller.demo.service

import com.proxyseller.demo.exception.EntityNullPointerException
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
        return repository.findAllSubscriptionsBySubscriberId(id)
                .stream()
                .map { it.targetUser }
                .toList()
    }

    List<User> findAllSubscribersByUserId(String id) {
        return repository.findAllSubscriptionsByTargetUserId(id)
                .stream()
                .map { it.subscriber }
                .toList()
    }

    Optional<Subscription> findByTargetUserIdAndSubscriberId(String userId, String subscriberId) {
        return repository.findByTargetUserIdAndSubscriberId(userId, subscriberId)
    }

    Subscription save(Subscription entity) {
        if (!entity.targetUser) {
            throw new EntityNullPointerException('null subscription targetUser value')
        }

        if (!entity.subscriber) {
            throw new EntityNullPointerException('null subscription subscriber value')
        }

        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
