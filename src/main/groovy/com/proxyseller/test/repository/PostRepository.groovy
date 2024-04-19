package com.proxyseller.test.repository

import com.proxyseller.test.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PostRepository extends JpaRepository<Post, Long> {

    @Transactional
    @Modifying
    @Query("SELECT p FROM Post p WHERE p.userData.id = ?1")
    List<Post> findAllByUserId(long id)
}
