package categories.dish.attribute

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DishAttributeController extends BaseController {

    DishAttributeService dishAttributeService

    def search(InputData inputData){
        try {
            def result = dishAttributeService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = dishAttributeService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DishAttribute dishDrinkAttribute = inputData?.item as DishAttribute
            dishAttributeService.save dishDrinkAttribute
            dishDrinkAttribute.refresh()
            def output = dishDrinkAttribute.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DishAttribute dishDrinkAttribute = DishAttribute.get(inputData?.item?.id as String)
            dishAttributeService.update inputData, dishDrinkAttribute
            dishDrinkAttribute.refresh()
            def output = dishDrinkAttribute.toJsonForm(inputData?.fbDatabase)

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
        DishAttribute dishDrinkAttribute = DishAttribute.get(id)

        if (dishDrinkAttribute == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            dishAttributeService.delete dishDrinkAttribute
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
