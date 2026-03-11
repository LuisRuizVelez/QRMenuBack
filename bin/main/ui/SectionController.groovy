package ui

import com.security.User
import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class SectionController extends BaseController {

    SectionService sectionService

    def search(InputData inputData){
        try {
            def result = sectionService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = sectionService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Section section = inputData?.item as Section
            sectionService.save section
            section.refresh()
            def output = section.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Section section = Section.get(inputData?.item?.id as String)
            sectionService.update inputData, section
            section.refresh()
            def output = section.toJsonForm()

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
        Section section = Section.get(id)

        if (section == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            sectionService.delete section
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }

    def getSectionsByUser(String username){
        try {
            User user = User.findByUsername(username)

            if (user == null) {
                render status: HttpStatus.BAD_REQUEST
                return
            }

            def sections = sectionService.getSectionsByUser user
            render sections as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }
}
