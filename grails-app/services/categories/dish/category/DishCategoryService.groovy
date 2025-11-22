package categories.dish.category

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData
import annotations.LangDomainClass
import annotations.UidDomainClass


@UidDomainClass( clazz = DishCategoryUid, mainAttribute='dishCategory')
@LangDomainClass( clazz = LangDishCategory, mainAttribute='dishCategory')
class DishCategoryService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<DishCategory> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<DishCategory> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params=[:]) {
        DishCategory dishDrinkCategory = inputData?.item as DishCategory

        return DishCategory.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')
            
            if (dishDrinkCategory.code != null)
                ilike 'code', "%${dishDrinkCategory.code}%"

            if (dishDrinkCategory.status != null)
                eq 'status' , dishDrinkCategory.status

        } as List<DishCategory>
    }

    @Transactional
    def save(DishCategory _dishDrinkCategory) {
        _dishDrinkCategory.groupingRole = _dishDrinkCategory.groupingRole ?: userService.getSessionValue('groupingRole') as Role
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
