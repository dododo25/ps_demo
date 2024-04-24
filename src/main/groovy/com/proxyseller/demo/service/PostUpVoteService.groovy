package com.proxyseller.demo.service

import com.proxyseller.demo.model.PostUpVote
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.PostUpVoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostUpVoteService {

    @Autowired
    private PostUpVoteRepository repository

    List<PostUpVote> findAll() {
        return repository.findAll()
    }

    List<Post> findAllPostsByUserId(String id) {
        return repository.findAllByUserId(id)
                .stream()
                .map { it.post }
                .toList()
    }

    List<User> findAllUsersByPostId(String id) {
        return repository.findAllByPostId(id)
                .stream()
                .map { it.user }
                .toList()
    }

    Optional<PostUpVote> findByPostIdAndUserId(String postId, String userId) {
        return repository.findByPostIdAndUserId(postId, userId)
    }

    PostUpVote save(PostUpVote entity) {
        if (!entity.user || !entity.post) {
            throw new NullPointerException()
        }

        return repository.save(entity)
    }

    void deleteById(String id) {
        repository.deleteById(id)
    }
}
