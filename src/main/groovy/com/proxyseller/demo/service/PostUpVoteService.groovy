package com.proxyseller.demo.service

import com.proxyseller.demo.model.PostUpVote
import com.proxyseller.demo.model.Post
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

    PostUpVote save(PostUpVote entity) {
        return repository.save(entity)
    }

    void delete(PostUpVote entity) {
        repository.delete(entity)
    }
}
