package com.proxyseller.demo.model

import org.springframework.data.annotation.Id

class Subscription {
    @Id String _id
    User targetUser
    User subscriber
}
