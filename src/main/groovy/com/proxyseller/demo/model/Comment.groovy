package com.proxyseller.demo.model

import org.springframework.data.annotation.Id

class Comment {
    @Id String _id
    Post post
    User commenter
    String content
}
