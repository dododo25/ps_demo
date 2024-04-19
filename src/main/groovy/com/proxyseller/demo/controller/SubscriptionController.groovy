package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Subscription
import com.proxyseller.demo.service.SubscriptionService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService

    @Autowired
    UserService userDataService

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def getAllUserSubscriptions(@PathVariable('userId') String userId) {
        return subscriptionService.findAllSubscriptionsByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def addUserSubscription(@PathVariable('userId') String userId, @RequestParam('targetId') String targetId) {
        Subscription subscription = new Subscription()

        subscription.targetUser = userDataService.findById(targetId).orElseThrow()
        subscription.subscriber = userDataService.findById(userId).orElseThrow()
        
        return subscriptionService.save(subscription)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{userId}/subscriptions')
    @ResponseBody
    def deleteUserSubscription(@PathVariable('userId') String userId, @RequestParam('targetId') String targetId) {
        Subscription subscription = new Subscription()

        subscription.targetUser = userDataService.findById(targetId).orElseThrow()
        subscription.subscriber = userDataService.findById(userId).orElseThrow()

        subscriptionService.delete(subscription)
    }
}
