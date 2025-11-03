package categories.drink.category

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkCategoryUid, mainAttribute = "drinkCategory" )
@LangDomainClass( clazz = LangDrinkCategory, mainAttribute = "drinkCategory" )
class DrinkCategoryService extends BaseService {

    def search(InputData inputData, Map params) {
        DrinkCategory drinkCategory = inputData?.item as DrinkCategory

        List<DrinkCategory> result = DrinkCategory.createCriteria().list(params) {
            if (drinkCategory.code != null)
                ilike 'code', "%${drinkCategory.code}%"

        } as List<DrinkCategory>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DrinkCategory.list()*.toBasicForm()
    }

    @Transactional
    def save(DrinkCategory _drinkCategory) {
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
