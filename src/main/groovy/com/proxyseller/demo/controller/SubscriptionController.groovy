package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Subscription
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.SubscriptionRepository
import com.proxyseller.demo.service.SubscriptionService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService

    @Autowired
    UserService userService

    @Autowired
    SubscriptionRepository subscriptionRepository

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def getAllUserSubscriptions(@PathVariable('userId') String userId) {
        return subscriptionService.findAllSubscriptionsByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/subscribers')
    @ResponseBody
    def getAllUserSubscribers(@PathVariable('userId') String userId) {
        return subscriptionService.findAllSubscribersByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def addUserSubscription(@PathVariable('userId') String subscriberId, @RequestParam('target') String targetId) {
        User targetUser = userService.findById(targetId).orElse(null)
        User subscriber = userService.findById(subscriberId).orElse(null)

        Optional<Subscription> subscription = subscriptionService
                .findByTargetUserIdAndSubscriberId(targetId, subscriberId)

        if (subscription.isPresent()) {
            return subscription
        }
        
        return subscriptionService.save(new Subscription(targetUser: targetUser, subscriber: subscriber))
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def deleteUserSubscription(@PathVariable('userId') String subscriberId, @RequestParam('target') String targetId) {
        subscriptionRepository.findByTargetUserIdAndSubscriberId(targetId, subscriberId)
                .ifPresent { subscriptionService.deleteById(it._id) }
    }
}
