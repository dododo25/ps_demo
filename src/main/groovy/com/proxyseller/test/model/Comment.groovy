package com.proxyseller.test.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @NotNull
    @JoinColumn(name = 'postId')
    @ManyToOne(targetEntity = Post)
    Post post

    @NotNull
    @JoinColumn(name = 'userId')
    @ManyToOne(targetEntity = UserData)
    UserData userData

    @NotNull
    @Column
    String content
}
