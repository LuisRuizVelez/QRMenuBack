package categories.dish.attribute

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishAttributeUid, mainAttribute = "dishAttribute" )
@LangDomainClass( clazz = LangDishAttribute, mainAttribute = "dishAttribute" )
class DishAttributeService extends BaseService {

    def search(InputData inputData, Map params) {
        DishAttribute dishDrinkAttribute = inputData?.item as DishAttribute

        List<DishAttribute> result = DishAttribute.createCriteria().list(params) {
            if (dishDrinkAttribute.code != null)
                ilike 'code', "%${dishDrinkAttribute.code}%"

        } as List<DishAttribute>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DishAttribute.list()*.toBasicForm()
    }

    @Transactional
    def save(DishAttribute _dishDrinkAttribute) {
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