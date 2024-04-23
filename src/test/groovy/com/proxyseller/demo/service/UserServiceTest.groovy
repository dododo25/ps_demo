package com.proxyseller.demo.service

import com.proxyseller.demo.model.User
import com.proxyseller.demo.repository.UserRepository
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class UserServiceTest extends Specification {

    @Autowired
    @Subject
    UserService userService

    @Autowired
    UserRepository userRepository

    def cleanup() {
        userRepository.deleteAll()
    }

    def "should save valid user"() {
        given:
        def validUser = new User(
                name: 'test_name',
                passwordHash: DigestUtils.sha256Hex('test_password'))

        when:
        def savedUser = userService.save(validUser)

        then:
        savedUser._id != null
    }

    def "should throw exception when user name is invalid"() {
        given:
        def invalidUser = new User(passwordHash: DigestUtils.sha256Hex('test_password'))

        when:
        userService.save(invalidUser)

        then:
        thrown(NullPointerException)
    }

    def "should throw exception when user password is invalid"() {
        given:
        def invalidUser = new User(name: 'test_name')

        when:
        userService.save(invalidUser)

        then:
        thrown(NullPointerException)
    }

    def "should find all users"() {
        when:
        def users = userService.findAll()

        then:
        users != null
    }

    def "should find user by id"() {
        given:
        def user = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
        def savedUser = userService.save(user)

        when:
        def userToFound = userService.findById(savedUser._id)

        then:
        savedUser._id == userToFound.map { it._id }.orElse(null)
    }

    def "should find user by name"() {
        given:
        def userName = 'test_name'
        def user = new User(name: userName, passwordHash: DigestUtils.sha256Hex('test_password'))
        def savedUser = userService.save(user)

        when:
        def userToFound = userService.findByName(userName)

        then:
        savedUser._id == userToFound.map { it._id }.orElse(null)
    }

    def "should delete user by id"() {
        given:
        def user = new User(name: 'test_name', passwordHash: DigestUtils.sha256Hex('test_password'))
        def savedUser = userService.save(user)

        when:
        userService.deleteById(savedUser._id)

        then:
        !userService.findById(savedUser._id).isPresent()
    }
}
