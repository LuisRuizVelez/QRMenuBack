package categories.dish.attribute

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishAttributeUid, mainAttribute = "dishAttribute" )
@LangDomainClass( clazz = LangDishAttribute, mainAttribute = "dishAttribute" )
class DishAttributeService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<DishAttribute> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<DishAttribute> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params=[:]) {
        DishAttribute dishDrinkAttribute = inputData?.item as DishAttribute

        return DishAttribute.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (dishDrinkAttribute.code != null)
                ilike 'code', "%${dishDrinkAttribute.code}%"

        } as List<DishAttribute>
    }

    @Transactional
    def save(DishAttribute _dishDrinkAttribute) {
        _dishDrinkAttribute.groupingRole = _dishDrinkAttribute.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        DishAttribute dishDrinkAttribute = _dishDrinkAttribute.save(flush: true, failOnError: true)

        if (!dishDrinkAttribute)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase dishDrinkAttribute

    }

    @Transactional
    def update(InputData inputData, DishAttribute dishDrinkAttribute) {
        dishDrinkAttribute.properties = inputData.item
        dishDrinkAttribute.save(flush: true, failOnError: true)

        sendToFirebase dishDrinkAttribute
    }

    @Transactional
    def delete(DishAttribute dishDrinkAttribute) {
        deleteFromFirebase dishDrinkAttribute

        dishDrinkAttribute.delete(flush: true)
    }
}