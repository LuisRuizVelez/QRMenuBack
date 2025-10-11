package categories.dish.category

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class DishCategoryController extends BaseController {

    DishCategoryService dishCategoryService

    def search(InputData inputData){
        try {
            def result = dishCategoryService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = dishCategoryService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            DishCategory dishDrinkCategory = inputData?.item as DishCategory
            dishCategoryService.save dishDrinkCategory
            dishDrinkCategory.refresh()
            def output = dishDrinkCategory.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            DishCategory dishDrinkCategory = DishCategory.get(inputData?.item?.id as String)
            dishCategoryService.update inputData, dishDrinkCategory
            dishDrinkCategory.refresh()
            def output = dishDrinkCategory.toJsonForm inputData?.fbDatabase

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
        DishCategory dishDrinkCategory = DishCategory.get(id)

        if (dishDrinkCategory == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            dishCategoryService.delete dishDrinkCategory
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
