package com.security


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class UserController extends BaseController {

    UserService userService

    def search(InputData inputData){
        try {
            def result = userService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = userService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            User user = inputData?.item as User
            userService.save user
            user.refresh()
            def output = user.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            User user = User.get(inputData?.item?.id as String)
            userService.update inputData, user
            user.refresh()
            def output = user.toJsonForm()

            render output as JSON
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def delete(String  id){
        User user = User.get(id)

        if (user == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            userService.delete user
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }

    def getRoles(String userId) {
        User user = User.get(userId)

        if (user == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        def roles = UserRole.findAllByUser(user)*.role
        render roles as JSON
    }

    @Transactional
    def addRole(String  userId, String roleId){
        User user = User.get(userId)
        Role role = Role.get(roleId)

        if (user == null || role == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            UserRole.create user, role, true
            user.refresh()

            render role as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }

    @Transactional
    def removeRole(String  userId, String roleId){
        User user = User.get(userId)
        Role role = Role.get(roleId)

        if (user == null || role == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            UserRole.remove user, role
            user.refresh()
            def output = user.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}

