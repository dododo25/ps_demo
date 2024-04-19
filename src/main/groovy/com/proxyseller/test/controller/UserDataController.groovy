package com.proxyseller.test.controller

import com.proxyseller.test.model.UserData

import com.proxyseller.test.service.UserDataService
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserDataController {

    @Autowired
    UserDataService service

    @RequestMapping(method = RequestMethod.GET, path = '/users')
    @ResponseBody
    def getAllUserData() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = '/users/{id}')
    @ResponseBody
    def getUserData(@PathVariable('id') int id) {
        return service.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.POST, path = '/users')
    @ResponseBody
    def addUserData(@RequestBody UserDataEntry data) {
        String passwordHash = DigestUtils.sha256Hex(data.password)
        return service.save(new UserData(name: data.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/users/{id}')
    @ResponseBody
    def editUserData(@PathVariable('id') int id, @RequestBody UserDataEntry data) {
        String passwordHash = DigestUtils.sha256Hex(data.password)
        return service.save(new UserData(id: id, name: data.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/users/{id}')
    @ResponseBody
    def deleteUserData(@PathVariable('id') int id) {
        service.deleteById(id)
    }

    private static class UserDataEntry {

        String name
        String password

    }
}
