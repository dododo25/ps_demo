package com.proxyseller.test.controller

import com.proxyseller.test.model.Post
import com.proxyseller.test.service.PostService
import com.proxyseller.test.service.UserDataService
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
    UserDataService userDataService

    @RequestMapping(method = RequestMethod.GET, path = '/posts')
    @ResponseBody
    List<Post> getAllPosts() {
        return postService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/posts/{id}')
    @ResponseBody
    Post getPost(@PathVariable('id') int id) {
        return postService.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/posts/{id}')
    @ResponseBody
    Post editPost(@PathVariable('id') int id, @RequestBody Post post) {
        post.id = id
        return postService.save(post)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/posts/{id}')
    @ResponseBody
    void deletePost(@PathVariable('id') int id) {
        postService.deleteById(id)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/posts')
    @ResponseBody
    Collection<Post> getAllUserPosts(@PathVariable('userId') int userId) {
        return postService.findAllByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/posts')
    @ResponseBody
    Post addPost(@PathVariable('userId') int userId, @RequestBody Post post) {
        post.userData = userDataService.findById(userId)
                .orElseThrow()
        return postService.save(post)
    }
}
