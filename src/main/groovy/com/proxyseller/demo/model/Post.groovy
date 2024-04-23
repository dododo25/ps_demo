package com.proxyseller.demo.model

import org.springframework.data.annotation.Id

class Post {
    @Id String _id
    User author
    String content
}
