package com.proxyseller.test.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @NotNull
    @Column(nullable = false)
    String name

    @NotNull
    @Column(nullable = false)
    String passwordHash

    @NotNull
    @JoinColumn(name = 'post_id')
    @OneToMany(targetEntity = Post)
    List<Post> posts

    UserData() {
        this.posts = new ArrayList<>()
    }
}
