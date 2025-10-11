package firebase


import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class FBChildPathController extends BaseController {

    @Autowired FBChildPathService fbChildPathService

    def search(InputData inputData){
        try {
            def result = fbChildPathService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = fbChildPathService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            FBChildPath fbChildPath = inputData?.item as FBChildPath
            fbChildPathService.save fbChildPath
            fbChildPath.refresh()
            def output = fbChildPath.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            FBChildPath fbChildPath = FBChildPath.get(inputData?.item?.id as String)
            fbChildPathService.update inputData, fbChildPath
            fbChildPath.refresh()
            def output = fbChildPath.toJsonForm(inputData?.fbDatabase)

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
        FBChildPath fbChildPath = FBChildPath.get(id)

        if (fbChildPath == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            fbChildPathService.delete fbChildPath
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}

