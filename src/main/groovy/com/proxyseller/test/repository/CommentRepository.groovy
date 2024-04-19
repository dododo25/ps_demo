package com.proxyseller.test.repository

import com.proxyseller.test.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = ?1")
    List<Comment> findAllByPostId(long id)

    @Query("SELECT c FROM Comment c WHERE c.userData.id = ?1")
    List<Comment> findAllByUserId(long id)
}