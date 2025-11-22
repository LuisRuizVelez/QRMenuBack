package categories.drink.presentation

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DrinkPresentationController extends BaseController {

    DrinkPresentationService drinkPresentationService

    def search(InputData inputData){
        try {
            def result = drinkPresentationService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = drinkPresentationService.getOptions inputData
        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DrinkPresentation drinkPresentation = inputData?.item as DrinkPresentation
            drinkPresentationService.save drinkPresentation
            drinkPresentation.refresh()
            def output = drinkPresentation.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DrinkPresentation drinkPresentation = DrinkPresentation.get(inputData?.item?.id as String)
            drinkPresentationService.update inputData, drinkPresentation
            drinkPresentation.refresh()
            def output = drinkPresentation.toJsonForm inputData?.fbDatabase

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
        DrinkPresentation drinkPresentation = DrinkPresentation.get(id)

        if (drinkPresentation == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            drinkPresentationService.delete drinkPresentation
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}