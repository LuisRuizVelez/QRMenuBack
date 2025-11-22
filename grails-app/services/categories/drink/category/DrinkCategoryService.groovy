package categories.drink.category

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkCategoryUid, mainAttribute = "drinkCategory" )
@LangDomainClass( clazz = LangDrinkCategory, mainAttribute = "drinkCategory" )
class DrinkCategoryService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<DrinkCategory> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<DrinkCategory> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params=[:]) {
        DrinkCategory drinkCategory = inputData?.item as DrinkCategory

        return DrinkCategory.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (drinkCategory.code != null)
                ilike 'code', "%${drinkCategory.code}%"

        } as List<DrinkCategory>
    }

    @Transactional
    def save(DrinkCategory _drinkCategory) {
        _drinkCategory.groupingRole = _drinkCategory.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        DrinkCategory drinkCategory = _drinkCategory.save(flush: true, failOnError: true)

        if (!drinkCategory)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase drinkCategory
    }

    @Transactional
    def update(InputData inputData, DrinkCategory drinkCategory) {
        drinkCategory.properties = inputData.item
        drinkCategory.save(flush: true, failOnError: true)

        sendToFirebase drinkCategory
    }

    @Transactional
    def delete(DrinkCategory drinkCategory) {
        deleteFromFirebase drinkCategory

        drinkCategory.delete(flush: true)
    }
}
