package com.proxyseller.demo.controller

import com.proxyseller.demo.model.Post
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    UserService userService

    @RequestMapping(method = RequestMethod.GET, path = '/posts')
    @ResponseBody
    def getAllPosts() {
        return postService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/posts/{id}')
    @ResponseBody
    def getPost(@PathVariable('id') String id) {
        return postService.findById(id).orElseGet { ResponseEntity.status(HttpStatus.BAD_REQUEST) }
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/posts/{id}')
    @ResponseBody
    def editPost(@PathVariable('id') String id, @RequestBody Post post) {
        post._id = id
        return postService.save(post)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/posts/{id}')
    @ResponseBody
    def deletePost(@PathVariable('id') String id) {
        postService.deleteById(id)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/posts')
    @ResponseBody
    def getAllUserPosts(@PathVariable('userId') String userId) {
        return postService.findAllByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/posts')
    @ResponseBody
    def addPost(@PathVariable('userId') String userId, @RequestBody Post post) {
        def user = userService.findById(userId).orElse(null)

        if (!user) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        }

        post.author = user
        return postService.save(post)
    }
}
