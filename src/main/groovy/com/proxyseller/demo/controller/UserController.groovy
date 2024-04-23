package com.proxyseller.demo.controller

import com.proxyseller.demo.model.User

import com.proxyseller.demo.service.UserService
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    UserService service

    @RequestMapping(method = RequestMethod.GET, path = '/users')
    @ResponseBody
    def getAllUsers() {
        return service.findAll()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{id}')
    @ResponseBody
    def getUser(@PathVariable('id') String id) {
        return service.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users', params = ['name'])
    @ResponseBody
    def getUserByName(@RequestParam('name') String name) {
        return service.findByName(name).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users')
    @ResponseBody
    def addUser(@RequestBody UserDataEntry data) {
        String passwordHash = DigestUtils.sha256Hex(data.password)
        return service.save(new User(name: data.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/users/{id}')
    @ResponseBody
    def editUser(@PathVariable('id') String id, @RequestBody UserDataEntry data) {
        String passwordHash = DigestUtils.sha256Hex(data.password)
        return service.save(new User(_id: id, name: data.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{id}')
    @ResponseBody
    def deleteUser(@PathVariable('id') String id) {
        service.deleteById(id)
    }

    private static class UserDataEntry {
        String name
        String password
    }
}
