package core.restaurant

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = RestaurantUid, mainAttribute = "restaurant" )
@LangDomainClass( clazz = LangRestaurant, mainAttribute = "restaurant" )
class RestaurantService extends BaseService {

    def search(InputData inputData, Map params) {
        Restaurant restaurant = inputData?.item as Restaurant

        List<Restaurant> result = Restaurant.createCriteria().list(params) {
            if (restaurant.code != null)
                ilike 'code', "%${restaurant.code}%"

        } as List<Restaurant>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Restaurant.list()*.toBasicForm()
    }

    @Transactional
    def save(Restaurant _restaurant) {
        Restaurant restaurant = _restaurant.save(flush: true, failOnError: true)

        if (!restaurant)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase restaurant
    }

    @Transactional
    def update(InputData inputData, Restaurant restaurant) {
        restaurant.properties = inputData.item
        restaurant.save(flush: true, failOnError: true)

        sendToFirebase restaurant
    }

    @Transactional
    def delete(Restaurant restaurant) {
        deleteFromFirebase restaurant

        restaurant.delete(flush: true)
    }
}
