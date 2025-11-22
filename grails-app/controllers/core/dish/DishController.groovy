package core.dish


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DishController extends BaseController {

    DishService dishService

    def search(InputData inputData){
        try {
            def result = dishService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = dishService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Dish dish = inputData?.item as Dish
            dishService.save dish
            dish.refresh()
            def output = dish.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Dish dish = Dish.get(inputData?.item?.id as String)
            dishService.update inputData, dish
            dish.refresh()
            def output = dish.toJsonForm inputData?.fbDatabase

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
        Dish dish = Dish.get(id)

        if (dish == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            dishService.delete dish
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
