package categories.restaurant

import grails.converters.JSON
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus

import bases.BaseController
import firebase.FBDatabase
import utils.InputData

class RestaurantCategoryController extends BaseController {

    RestaurantCategoryService restaurantCategoryService

    def search(InputData inputData, Integer max){
        if (inputData.item == null) {
            render status: HttpStatus.UNPROCESSABLE_ENTITY
            return
        }

        params.max = max ?: 1000
        def result = restaurantCategoryService.search(inputData, params as Map)
        render result as JSON
    }


    def getOptions(){
        def result = restaurantCategoryService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            RestaurantCategory restaurantCategory = (RestaurantCategory) inputData?.item
            restaurantCategoryService.save restaurantCategory
            restaurantCategory.refresh()
            def output = restaurantCategory.toJsonForm(inputData?.fbDatabase)

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            RestaurantCategory restaurantCategory = RestaurantCategory.get(inputData?.item?.id as String)
            restaurantCategoryService.update(inputData, restaurantCategory)
            restaurantCategory.refresh()
            def output = restaurantCategory.toJsonForm(inputData?.fbDatabase)

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
        RestaurantCategory restaurantCategory = RestaurantCategory.get(id)

        if (restaurantCategory == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            restaurantCategoryService.delete restaurantCategory
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}



