package com.proxyseller.test.repository

import com.proxyseller.test.model.FavouritePost
import com.proxyseller.test.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FavouritePostRepository extends JpaRepository<FavouritePost, Long> {

    @Query("SELECT fp.id.post FROM FavouritePost fp WHERE fp.id.userData.id = ?1")
    List<Post> findAllByUserId(long id)

    @Transactional
    @Modifying
    @Query("DELETE FROM FavouritePost fp WHERE fp.id.userData.id = ?1 AND fp.id.post.id = ?2")
    void delete(long userId, long postId)
}
