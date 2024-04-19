package com.proxyseller.test.controller

import com.proxyseller.test.model.Post
import com.proxyseller.test.model.UserData
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

    @RequestMapping(method = RequestMethod.GET, path = '/post')
    @ResponseBody
    List<Post> getAllPosts() {
        return postService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = '/post/{id}')
    @ResponseBody
    Post getPost(@PathVariable('id') int id) {
        return postService.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/user/{userId}/post')
    @ResponseBody
    List<Post> getAllUserPosts(@PathVariable('userId') int userId) {
        return userDataService.findById(userId).map { ud -> ud.posts }
                .orElseThrow()
    }

    @RequestMapping(method = RequestMethod.POST, path = '/user/{userId}/post')
    @ResponseBody
    Post addPost(@PathVariable('userId') int userId, @RequestBody Post post) {
        UserData data = userDataService.findById(userId)
            .orElseThrow()
        data.posts.add(post)
        return postService.save(post)
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/user/{userId}/post/{id}')
    @ResponseBody
    Post editPost(@PathVariable('userId') int userId, @PathVariable('id') int id, @RequestBody Post post) {
        UserData data = userDataService.findById(userId)
                .orElseThrow()

        post.id = id
        data.posts.add(post)

        return postService.save(post)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/user/{userId}/post/{id}')
    @ResponseBody
    void deletePost(@PathVariable('userId') int userId, @PathVariable('id') int id) {
        UserData data = userDataService.findById(userId)
                .orElseThrow()
        data.posts.removeIf {post -> post.id == (long) id }
        postService.deleteById(id)
    }
}
