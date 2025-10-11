
import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class $MODEL_NAME$Controller extends BaseController {

    $MODEL_NAME$Service $SERVICE$Service

    def search(InputData inputData){
        try {
            def result = $SERVICE$Service.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = $SERVICE$Service.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            $MODEL_NAME$ $MODEL$ = inputData?.item as $MODEL_NAME$
            $SERVICE$Service.save $MODEL$
            $MODEL$.refresh()
            def output = $MODEL$.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            $MODEL_NAME$ $MODEL$ = $MODEL_NAME$.get(inputData?.item?.id as String)
            $SERVICE$Service.update inputData, $MODEL$
            $MODEL$.refresh()
            def output = $MODEL$.toJsonForm inputData?.fbDatabase

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
        $MODEL_NAME$ $MODEL$ = $MODEL_NAME$.get(id)

        if ($MODEL$ == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            $SERVICE$Service.delete $MODEL$
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
