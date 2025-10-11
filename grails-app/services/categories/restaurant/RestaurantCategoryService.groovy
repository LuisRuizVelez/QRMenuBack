package categories.restaurant

import annotations.LangDomainClass
import annotations.UidDomainClass
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData


@UidDomainClass(clazz = RestaurantCategoryUid, mainAttribute='restaurantCategory')
@LangDomainClass(clazz = LangRestaurantCategory, mainAttribute='restaurantCategory')
class RestaurantCategoryService extends BaseService {

    def search(InputData inputData, Map params) {
        RestaurantCategory restaurantCategory = inputData?.item as RestaurantCategory

        List<RestaurantCategory> result = RestaurantCategory.createCriteria().list(params) {
            if (restaurantCategory.code != null)
                ilike 'code', "%${restaurantCategory.code}%"

        } as List<RestaurantCategory>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        RestaurantCategory.list()*.toBasicForm()
    }

    @Transactional
    def save(RestaurantCategory _restaurantCategory) {
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