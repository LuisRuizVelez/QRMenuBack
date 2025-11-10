package com.security


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import ui.Section
import ui.SectionRole
import utils.InputData

class RoleController extends BaseController {

    RoleService roleService

    def search(InputData inputData){
        try {
            def result = roleService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = roleService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Role role = inputData?.item as Role
            roleService.save role
            role.refresh()
            def output = role.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Role role = Role.get(inputData?.item?.id as String)
            roleService.update inputData, role
            role.refresh()
            def output = role.toJsonForm()

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
        Role role = Role.get(id)

        if (role == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            roleService.delete role
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }

    @Transactional
    def addSection(){
        try {
            Role role = Role.get(params.roleId as String)
            Section section = Section.get(params.sectionId as String)

            if(!role || !section)
                throw new Exception("Role or Section not found.")

            SectionRole sectionRole = new SectionRole(role: role, section: section)
            sectionRole.save(flush:true, failOnError:true)

            role.refresh()

            def output = section.toBasicForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }


    def getSections(Integer roleId){
        try {
            Role role = Role.get(roleId)

            if(!role)
                throw new Exception("Role not found.")

            def sections = SectionRole.findAllByRole(role)?.collect { it.section.toBasicForm() } ?: []

            render sections as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }


    @Transactional
    def addSections(String roleId, String sectionIds){
        try {
            Role role = Role.get(roleId)
            Section section = Section.get(sectionIds)

            if(!role || !sectionIds)
                throw new Exception("Role or Section IDs not found.")

            SectionRole sectionRole = new SectionRole(role: role, section: section)
            sectionRole.save(flush: true, failOnError: true)

            def output = section.toBasicForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def removeSection(String roleId, String sectionId){
        try {
            Role role = Role.get(roleId)
            Section section = Section.get(sectionId)

            if(!role || !section)
                throw new Exception("Role or Section not found.")

            SectionRole sectionRole = SectionRole.findByRoleAndSection(role, section)

            if(!sectionRole)
                throw new Exception("Section not assigned to the role.")

            sectionRole.delete(flush:true, failOnError:true)

            role.refresh()

            def output = role.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }
}

