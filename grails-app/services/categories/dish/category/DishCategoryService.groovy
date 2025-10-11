package categories.dish.category

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData
import annotations.LangDomainClass
import annotations.UidDomainClass


@UidDomainClass( clazz = DishCategoryUid, mainAttribute='dishCategory')
@LangDomainClass( clazz = LangDishCategory, mainAttribute='dishCategory')
class DishCategoryService extends BaseService {

    def search(InputData inputData, Map params) {
        DishCategory dishDrinkCategory = inputData?.item as DishCategory

        List<DishCategory> result = DishCategory.createCriteria().list(params) {
            if (dishDrinkCategory.code != null)
                ilike 'code', "%${dishDrinkCategory.code}%"

            if (dishDrinkCategory.status != null)
                eq 'status' , dishDrinkCategory.status

        } as List<DishCategory>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DishCategory.list()*.toBasicForm()
    }

    @Transactional
    def save(DishCategory _dishDrinkCategory) {
        DishCategory dishDrinkCategory = _dishDrinkCategory.save(flush: true, failOnError: true)

        if (!dishDrinkCategory)
            throw new Exception("Unable to save object.")

        sendToFirebase dishDrinkCategory
    }

    @Transactional
    def update(InputData inputData, DishCategory dishDrinkCategory) {
        dishDrinkCategory.properties = inputData.item
        dishDrinkCategory.save(flush: true, failOnError: true)

        sendToFirebase dishDrinkCategory
    }

    @Transactional
    def delete(DishCategory dishDrinkCategory) {
        deleteFromFirebase dishDrinkCategory
        dishDrinkCategory.delete(flush: true)
    }
}
