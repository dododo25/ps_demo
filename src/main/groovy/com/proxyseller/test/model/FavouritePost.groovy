package com.proxyseller.test.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity

@Entity
class FavouritePost {

    @EmbeddedId
    FavouritePostId id

    FavouritePost() {
        this.id = new FavouritePostId()
    }

    void setUserData(UserData data) {
        this.id.userData = data
    }

    void setPost(Post post) {
        this.id.post = post
    }
}
