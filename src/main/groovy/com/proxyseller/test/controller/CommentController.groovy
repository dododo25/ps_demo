package com.proxyseller.test.controller

import com.proxyseller.test.model.Comment
import com.proxyseller.test.service.CommentService
import com.proxyseller.test.service.PostService
import com.proxyseller.test.service.UserDataService
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
    UserDataService userDataService

    @RequestMapping(method = RequestMethod.GET, path = '/comments')
    @ResponseBody
    List<Comment> getAllComments() {
        return commentService.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/comments/{id}')
    @ResponseBody
    Comment getComment(@PathVariable('id') int id) {
        return commentService.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/comments/{id}')
    @ResponseBody
    Comment editComment(@PathVariable('id') int id, @RequestBody Comment comment) {
        comment.id = id
        return commentService.save(comment)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/comments/{id}')
    @ResponseBody
    void deleteComment(@PathVariable('id') int id) {
        commentService.deleteById(id)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/comments')
    @ResponseBody
    List<Comment> getAllUserComments(@PathVariable('userId') int userId) {
        return commentService.findAllByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.GET, path = '/posts/{postId}/comments')
    @ResponseBody
    List<Comment> getAllPostComments(@PathVariable('postId') int postId) {
        return commentService.findAllByPostId(postId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/posts/{postId}/comments')
    @ResponseBody
    Comment addComment(@RequestParam('userId') int userId,
                       @PathVariable('postId') int postId,
                       @RequestBody Comment comment) {
        comment.userData = userDataService.findById(userId)
                .orElseThrow()
        comment.post = postService.findById(postId)
                .orElseThrow()

        return commentService.save(comment)
    }
}
