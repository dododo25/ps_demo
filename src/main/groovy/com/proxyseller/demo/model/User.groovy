package com.proxyseller.demo.model

import org.springframework.data.annotation.Id

class User {
    @Id String _id
    String name
    String passwordHash
}
