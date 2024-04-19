package com.proxyseller.test.repository

import com.proxyseller.test.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.userData.id = ?1")
    List<Post> findAllByUserId(long id)
}
