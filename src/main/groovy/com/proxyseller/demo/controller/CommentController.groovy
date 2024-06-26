package com.proxyseller.demo.controller

import com.proxyseller.demo.exception.BadRequestException
import com.proxyseller.demo.model.Comment
import com.proxyseller.demo.model.Post
import com.proxyseller.demo.model.User
import com.proxyseller.demo.service.CommentService
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class CommentController {

    @Autowired
    CommentService commentService

    @Autowired
    PostService postService

    @Autowired
    UserService userService

    @RequestMapping(method = RequestMethod.GET, path = '/comments')
    @ResponseBody
    def getAllComments() {
        return commentService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/comments/{id}')
    @ResponseBody
    def getComment(@PathVariable('id') String id) {
        return commentService.findById(id).orElseThrow { new BadRequestException("unknown comment id") }
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/comments/{id}')
    @ResponseBody
    def updateComment(@PathVariable('id') String id, @RequestBody Comment comment) {
        comment._id = id
        return commentService.save(comment)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/comments/{id}')
    @ResponseBody
    def deleteComment(@PathVariable('id') String id) {
        commentService.deleteById(id)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/comments')
    @ResponseBody
    def getAllUserComments(@PathVariable('userId') String userId) {
        return commentService.findAllByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/posts/{postId}/comments')
    @ResponseBody
    def getAllPostComments(@PathVariable('postId') String postId) {
        return commentService.findAllByPostId(postId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/posts/{postId}/comments', params = ['user'])
    @ResponseBody
    def addComment(@RequestParam('user') String userId,
                   @PathVariable('postId') String postId,
                   @RequestBody Comment comment) {
        User commenter = userService.findById(userId).orElse(null)
        Post post = postService.findById(postId).orElse(null)

        comment.commenter = commenter
        comment.post = post
        comment.creationDate = new Date()

        return commentService.save(comment)
    }
}
