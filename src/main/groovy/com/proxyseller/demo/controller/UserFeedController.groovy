package com.proxyseller.demo.controller

import com.proxyseller.demo.repository.PostUpVoteRepository
import com.proxyseller.demo.service.CommentService
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UserFeedController {

    @Autowired
    UserService userService

    @Autowired
    PostService postService

    @Autowired
    CommentService commentService

    @Autowired
    PostUpVoteRepository postUpVoteRepository

    @RequestMapping(method = RequestMethod.GET, path = '/users/{id}/feed')
    @ResponseBody
    def getFeedForUserId(@PathVariable('id') String id) {
        def posts = postService.findAllByUserId(id)
        def comments = commentService.findAllByUserId(id)
        def postUpVotes = postUpVoteRepository.findAllByUserId(id)

        def feedList = new ArrayList<FeedItem>()

        addObjectsToFeedList(feedList, posts, post -> new FeedItem(entity: post, type: "post"))
        addObjectsToFeedList(feedList, comments, comment -> new FeedItem(entity: comment, type: "comment"))
        addObjectsToFeedList(feedList, postUpVotes, upVote -> new FeedItem(entity: null, type: "upvote"))

        return feedList
    }

    static def addObjectsToFeedList(feedList, objects, function) {
        for (Object obj : objects) {
            boolean inserted = false

            for (int i = 0; i < feedList.size(); i++) {
                if (obj.creationDate > feedList.get(i).entity.creationDate) {
                    feedList.add(i, function(obj))
                    inserted = true
                    break
                }
            }

            if (!inserted) {
                feedList.add(function(obj))
            }
        }
    }

    private static class FeedItem {
        Object entity
        String type
    }
}
