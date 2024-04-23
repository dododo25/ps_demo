package com.proxyseller.demo.model

import org.springframework.data.annotation.Id

class PostUpVote {
    @Id String _id
    User user
    Post post
}
