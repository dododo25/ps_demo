package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Post
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class PostController {

    @Autowired
    PostService postService

    @Autowired
    UserService userDataService

    @RequestMapping(method = RequestMethod.GET, path = '/posts')
    @ResponseBody
    List<Post> getAllPosts() {
        return postService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/posts/{id}')
    @ResponseBody
    Post getPost(@PathVariable('id') String id) {
        return postService.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/posts/{id}')
    @ResponseBody
    Post editPost(@PathVariable('id') String id, @RequestBody Post post) {
        post._id = id
        return postService.save(post)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/posts/{id}')
    @ResponseBody
    void deletePost(@PathVariable('id') String id) {
        postService.deleteById(id)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/posts')
    @ResponseBody
    List<Post> getAllUserPosts(@PathVariable('userId') String userId) {
        return postService.findAllByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/posts')
    @ResponseBody
    Post addPost(@PathVariable('userId') String userId, @RequestBody Post post) {
        post.author = userDataService.findById(userId)
                .orElseThrow()
        return postService.save(post)
    }
}
