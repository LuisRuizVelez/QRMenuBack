package categories.dish.presentation

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DishPresentationController extends BaseController {

    DishPresentationService dishPresentationService

    def search(InputData inputData){
        try {
            def result = dishPresentationService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = dishPresentationService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DishPresentation presentationType = inputData?.item as DishPresentation
            dishPresentationService.save presentationType
            presentationType.refresh()
            def output = presentationType.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DishPresentation presentationType = DishPresentation.get(inputData?.item?.id as String)
            dishPresentationService.update inputData, presentationType
            presentationType.refresh()
            def output = presentationType.toJsonForm inputData?.fbDatabase

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
        DishPresentation presentationType = DishPresentation.get(id)

        if (presentationType == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            dishPresentationService.delete presentationType
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}