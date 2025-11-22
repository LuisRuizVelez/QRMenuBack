package categories.restaurant

import annotations.LangDomainClass
import annotations.UidDomainClass
import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData


@UidDomainClass(clazz = RestaurantCategoryUid, mainAttribute='restaurantCategory')
@LangDomainClass(clazz = LangRestaurantCategory, mainAttribute='restaurantCategory')
class RestaurantCategoryService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<RestaurantCategory> result =filterData inputData, params
        [total: result?.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<RestaurantCategory> result =filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params= [:]) {
        RestaurantCategory restaurantCategory = inputData?.item as RestaurantCategory

        return RestaurantCategory.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (restaurantCategory.code != null)
                ilike 'code', "%${restaurantCategory.code}%"

        } as List<RestaurantCategory>
    }

    @Transactional
    def save(RestaurantCategory _restaurantCategory) {
        _restaurantCategory.groupingRole = _restaurantCategory.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        RestaurantCategory restaurantCategory = _restaurantCategory.save(flush: true, failOnError: true)

        if (!restaurantCategory)
            throw new Exception("Unable to save object.")

        sendToFirebase restaurantCategory
    }

    @Transactional
    def update(InputData inputData, RestaurantCategory restaurantCategory) {
        restaurantCategory.properties = inputData.item
        restaurantCategory.save(flush: true, failOnError: true)

        sendToFirebase restaurantCategory
    }

    @Transactional
    def delete(RestaurantCategory restaurantCategory) {
        deleteFromFirebase restaurantCategory
        restaurantCategory.delete(flush: true)
    }
}