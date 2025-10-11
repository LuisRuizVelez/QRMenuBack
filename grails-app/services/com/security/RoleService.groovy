package com.security

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class RoleService extends BaseService {

    def search(InputData inputData, Map params) {
        Role role = inputData?.item as Role

        List<Role> result = Role.createCriteria().list(params) {
            if (role.authority != null)
                ilike 'authority', "%${role.authority}%"

        } as List<Role>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Role.list()*.toBasicForm()
    }

    @Transactional
    def save(Role _role) {
        Role role = _role.save(flush: true, failOnError: true)

        if (!role)
            throw new Exception("521 : Unable to save object.")

    }

    @Transactional
    def update(InputData inputData, Role role) {
        role.properties = inputData.item
        role.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(Role role) {
        role.delete(flush: true)
    }
}