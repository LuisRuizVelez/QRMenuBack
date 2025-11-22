package categories.drink.category


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DrinkCategoryController extends BaseController {

    DrinkCategoryService drinkCategoryService

    def search(InputData inputData){
        try {
            def result = drinkCategoryService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = drinkCategoryService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DrinkCategory drinkCategory = inputData?.item as DrinkCategory
            drinkCategoryService.save drinkCategory
            drinkCategory.refresh()
            def output = drinkCategory.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DrinkCategory drinkCategory = DrinkCategory.get(inputData?.item?.id as String)
            drinkCategoryService.update inputData, drinkCategory
            drinkCategory.refresh()
            def output = drinkCategory.toJsonForm inputData?.fbDatabase

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
        DrinkCategory drinkCategory = DrinkCategory.get(id)

        if (drinkCategory == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            drinkCategoryService.delete drinkCategory
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}