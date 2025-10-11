package catalogs.lang


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class LangController extends BaseController {

    LangService langService

    def search(InputData inputData){
        try {
            def result = langService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = langService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Lang lang = inputData?.item as Lang
            langService.save(lang)
            lang.refresh()
            def output = lang.toJsonForm(inputData?.fbDatabase)

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Lang lang = Lang.get(inputData?.item?.id as String)
            langService.update(inputData, lang)
            lang.refresh()
            def output = lang.toJsonForm(inputData?.fbDatabase)

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
        Lang lang = Lang.get(id)

        if (lang == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            langService.delete(lang)
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
