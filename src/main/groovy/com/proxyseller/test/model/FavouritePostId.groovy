package com.proxyseller.test.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Embeddable
class FavouritePostId {

    @NotNull
    @JoinColumn(name = 'userId')
    @ManyToOne(targetEntity = UserData)
    UserData userData

    @NotNull
    @JoinColumn(name = 'postId')
    @ManyToOne(targetEntity = Post)
    Post post

    @Override
    int hashCode() {
        return Objects.hash(userData, post)
    }

    @Override
    boolean equals(Object obj) {
        return obj == this || obj instanceof FavouritePostId
                && ((FavouritePostId) obj).userData == userData
                && ((FavouritePostId) obj).post == post
    }
}
