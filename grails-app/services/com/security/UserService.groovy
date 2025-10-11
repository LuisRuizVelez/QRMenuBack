package com.security

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class UserService extends BaseService {

    def search(InputData inputData, Map params) {
        User user = inputData?.item as User

        List<User> result = User.createCriteria().list(params) {
            if (user.username != null)
                ilike 'username', "%${user.username}%"

            if (user.enabled != null)
                eq 'enabled', user.enabled

        } as List<User>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        User.list()*.toBasicForm()
    }

    @Transactional
    def save(User _user) {
        User user = _user.save(flush: true, failOnError: true)

        if (!user)
            throw new Exception("521 : Unable to save object.")

    }

    @Transactional
    def update(InputData inputData, User user) {
        user.properties = inputData.item
        user.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(User user) {
        user.delete(flush: true)
    }
}
