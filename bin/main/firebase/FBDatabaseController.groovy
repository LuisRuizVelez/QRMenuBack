package firebase


import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class FBDatabaseController extends BaseController {

    @Autowired FBDatabaseService fbDatabaseService

    def search(InputData inputData){
        try {
            def result = fbDatabaseService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = fbDatabaseService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            FBDatabase fBDatabase = inputData?.item as FBDatabase
            fbDatabaseService.save fBDatabase
            fBDatabase.refresh()
            def output = fBDatabase.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            FBDatabase fBDatabase = FBDatabase.get(inputData?.item?.id as String)
            fbDatabaseService.update inputData, fBDatabase
            fBDatabase.refresh()
            def output = fBDatabase.toJsonForm(inputData?.fbDatabase)

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
        FBDatabase fBDatabase = FBDatabase.get(id)

        if (fBDatabase == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            fbDatabaseService.delete fBDatabase
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
