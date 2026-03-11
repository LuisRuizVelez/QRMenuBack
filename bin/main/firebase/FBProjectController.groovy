package firebase


import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class FBProjectController extends BaseController {

    @Autowired FBProjectService fBProjectService

    def search(InputData inputData){
        try {
            def result = fBProjectService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = fBProjectService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            FBProject fbProject = inputData?.item as FBProject
            fBProjectService.save fbProject
            fbProject.refresh()
            def output = fbProject.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            FBProject fbProject = FBProject.get(inputData?.item?.id as String)
            fBProjectService.update inputData, fbProject
            fbProject.refresh()
            def output = fbProject.toJsonForm(inputData?.fbDatabase)

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
        FBProject fbProject = FBProject.get(id)

        if (fbProject == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            fBProjectService.delete fbProject
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
