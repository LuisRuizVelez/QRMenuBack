package core.restaurant


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class RestaurantController extends BaseController {

    RestaurantService restaurantService

    def search(InputData inputData){
        try {
            def result = restaurantService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = restaurantService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Restaurant restaurant = inputData?.item as Restaurant
            restaurantService.save restaurant
            restaurant.refresh()
            def output = restaurant.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Restaurant restaurant = Restaurant.get(inputData?.item?.id as String)
            restaurantService.update inputData, restaurant
            restaurant.refresh()
            def output = restaurant.toJsonForm inputData?.fbDatabase

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
        Restaurant restaurant = Restaurant.get(id)

        if (restaurant == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            restaurantService.delete restaurant
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}