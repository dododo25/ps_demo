package com.proxyseller.test.controller

import com.proxyseller.test.model.UserData
import com.proxyseller.test.model.UserDataEntry
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

    @RequestMapping(method = RequestMethod.GET, path = '/user')
    @ResponseBody
    def getAllUserData() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = '/user/{id}')
    @ResponseBody
    def getUserData(@PathVariable('id') int id) {
        return service.findById(id).orElseThrow()
    }

    @RequestMapping(method = RequestMethod.POST, path = '/user')
    @ResponseBody
    def addUserData(@RequestBody UserDataEntry entry) {
        String passwordHash = DigestUtils.sha256Hex(entry.password)
        return service.save(new UserData(name: entry.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.PUT, path = '/user/{id}')
    @ResponseBody
    def editUserData(@PathVariable('id') int id, @RequestBody UserDataEntry entry) {
        String passwordHash = DigestUtils.sha256Hex(entry.password)
        return service.save(new UserData(id: id, name: entry.name, passwordHash: passwordHash))
    }

    @RequestMapping(method = RequestMethod.DELETE, path = '/user/{id}')
    @ResponseBody
    def deleteUserData(@PathVariable('id') int id) {
        service.deleteById(id)
    }
}
