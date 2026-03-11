package core.dish


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DishPriceController extends BaseController {

    DishPriceService dishPriceService

    def search(InputData inputData){
        try {
            def result = dishPriceService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = dishPriceService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DishPrice dishPrice = inputData?.item as DishPrice
            dishPriceService.save dishPrice
            dishPrice.refresh()
            def output = dishPrice.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DishPrice dishPrice = DishPrice.get(inputData?.item?.id as String)
            dishPriceService.update inputData, dishPrice
            dishPrice.refresh()
            def output = dishPrice.toJsonForm inputData?.fbDatabase

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
        DishPrice dishPrice = DishPrice.get(id)

        if (dishPrice == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            dishPriceService.delete dishPrice
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
