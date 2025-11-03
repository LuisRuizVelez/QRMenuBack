package core.drink

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DrinkPriceController extends BaseController {

    DrinkPriceService drinkPriceService

    def search(InputData inputData){
        try {
            def result = drinkPriceService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = drinkPriceService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DrinkPrice drinkPrice = inputData?.item as DrinkPrice
            drinkPriceService.save drinkPrice
            drinkPrice.refresh()
            def output = drinkPrice.toJsonForm()

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DrinkPrice drinkPrice = DrinkPrice.get(inputData?.item?.id as String)
            drinkPriceService.update inputData, drinkPrice
            drinkPrice.refresh()
            def output = drinkPrice.toJsonForm()

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
        DrinkPrice drinkPrice = DrinkPrice.get(id)

        if (drinkPrice == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            drinkPriceService.delete drinkPrice
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}