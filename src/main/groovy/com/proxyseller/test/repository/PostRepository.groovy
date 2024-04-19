package com.proxyseller.test.repository

import com.proxyseller.test.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends JpaRepository<Post, Long> {

}
