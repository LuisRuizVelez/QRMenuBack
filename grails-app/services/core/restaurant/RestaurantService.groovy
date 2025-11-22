package core.restaurant

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = RestaurantUid, mainAttribute = "restaurant" )
@LangDomainClass( clazz = LangRestaurant, mainAttribute = "restaurant" )
class RestaurantService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<Restaurant> result = filterData inputData, params
        [total: result?.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<Restaurant> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params = [:]) {
        Restaurant restaurant = inputData?.item as Restaurant

        return Restaurant.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (restaurant.code != null)
                ilike 'code', "%${restaurant.code}%"
        } as List<Restaurant>
    }

    @Transactional
    def save(Restaurant _restaurant) {
        _restaurant.groupingRole = _restaurant.groupingRole ?: userService.getSessionValue('groupingRole') as Role
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
