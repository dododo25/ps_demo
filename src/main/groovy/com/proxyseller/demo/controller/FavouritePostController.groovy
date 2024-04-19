package com.proxyseller.demo.controller

import com.proxyseller.demo.model.FavouritePost
import com.proxyseller.demo.service.FavouritePostService
import com.proxyseller.demo.service.PostService
import com.proxyseller.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class FavouritePostController {

    @Autowired
    FavouritePostService favouritePostService

    @Autowired
    UserService userDataService

    @Autowired
    PostService postService

    @RequestMapping(method = RequestMethod.GET, path = '/users/{userId}/favourites')
    @ResponseBody
    def getAllUserFavouritePosts(@PathVariable('userId') String userId) {
        return favouritePostService.findAllPostsByUserId(userId)
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users/{userId}/favourites')
    @ResponseBody
    def addUserFavouritePost(@PathVariable('userId') String userId, @RequestParam('postId') String postId) {
        FavouritePost favouritePost = new FavouritePost()

        favouritePost.setUser(userDataService.findById(userId)
                .orElseThrow())
        favouritePost.setPost(postService.findById(postId)
                .orElseThrow())
        
        return favouritePostService.save(favouritePost)
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{userId}/favourites')
    @ResponseBody
    def deleteUserFavouritePost(@PathVariable('userId') String userId, @RequestParam('postId') String postId) {
        FavouritePost favouritePost = new FavouritePost()

        favouritePost.setUser(userDataService.findById(userId)
                .orElseThrow())
        favouritePost.setPost(postService.findById(postId)
                .orElseThrow())

        favouritePostService.delete(favouritePost)
    }
}
