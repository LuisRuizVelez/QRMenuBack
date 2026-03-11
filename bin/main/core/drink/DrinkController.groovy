package core.drink


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DrinkController extends BaseController {

    DrinkService drinkService

    def search(InputData inputData){
        try {
            def result = drinkService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = drinkService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Drink drink = inputData?.item as Drink
            drinkService.save drink
            drink.refresh()
            def output = drink.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Drink drink = Drink.get(inputData?.item?.id as String)
            drinkService.update inputData, drink
            drink.refresh()
            def output = drink.toJsonForm inputData?.fbDatabase

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
        Drink drink = Drink.get(id)

        if (drink == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            drinkService.delete drink
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}