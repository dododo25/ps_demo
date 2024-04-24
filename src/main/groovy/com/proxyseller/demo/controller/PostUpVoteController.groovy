package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.PostUpVote
import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.PostUpVoteRepository
import com.proxyseller.demo.service.PostUpVoteService
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PostUpVoteController {

    @Autowired
    PostUpVoteService postVoteUpService

    @Autowired
    UserService userService

    @Autowired
    PostService postService

    @Autowired
    PostUpVoteRepository postVoteUpRepository

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/favourites')
    @ResponseBody
    def getAllUserFavouritePosts(@PathVariable('userId') String userId) {
        return postVoteUpService.findAllPostsByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/favourites')
    @ResponseBody
    def addUserFavouritePost(@PathVariable('userId') String userId, @RequestParam('post') String postId) {
        User user = userService.findById(userId).orElse(null)
        Post post = postService.findById(postId).orElse(null)

        if (!user || !post) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        }

        Optional<PostUpVote> upVote = postVoteUpService.findByPostIdAndUserId(postId, userId)

        if (upVote.isPresent()) {
            return upVote
        }

        return postVoteUpService.save(new PostUpVote(user: user, post: post))
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{userId}/favourites')
    @ResponseBody
    def deleteUserFavouritePost(@PathVariable('userId') String userId, @RequestParam('post') String postId) {
       postVoteUpRepository.findByPostIdAndUserId(postId, userId)
               .ifPresent { postVoteUpService.deleteById(it._id) }
    }
}
