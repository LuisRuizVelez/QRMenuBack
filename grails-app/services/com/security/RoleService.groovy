package com.security

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class RoleService extends BaseService {

    def search(InputData inputData, Map params) {
        List<Role> result = filterRoles inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }


    def getOptions(InputData inputData) {
        List<Role> roles = filterRoles(inputData)
        roles*.toBasicForm()
    }


    def filterRoles(InputData inputData, Map params = [:]) {
        Role role = inputData?.item as Role

        return Role.createCriteria().list(params) {
            if (role.authority != null)
                ilike 'authority', "%${role.authority}%"

            if (role.isToGrouping != null)
                eq 'isToGrouping', role.isToGrouping

        } as List<Role>
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