package categories.drink.presentation

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkPresentationUid, mainAttribute = "drinkPresentation" )
@LangDomainClass( clazz = LangDrinkPresentation, mainAttribute = "drinkPresentation" )
class DrinkPresentationService extends BaseService {

    def search(InputData inputData, Map params) {
        DrinkPresentation drinkPresentation = inputData?.item as DrinkPresentation

        List<DrinkPresentation> result = DrinkPresentation.createCriteria().list(params) {
            if (drinkPresentation.code != null)
                ilike 'code', "%${drinkPresentation.code}%"

        } as List<DrinkPresentation>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DrinkPresentation.list()*.toBasicForm()
    }

    @Transactional
    def save(DrinkPresentation _drinkPresentation) {
        DrinkPresentation drinkPresentation = _drinkPresentation.save(flush: true, failOnError: true)

        if (!drinkPresentation)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase drinkPresentation
    }

    @Transactional
    def update(InputData inputData, DrinkPresentation drinkPresentation) {
        drinkPresentation.properties = inputData.item
        drinkPresentation.save(flush: true, failOnError: true)

        sendToFirebase drinkPresentation
    }

    @Transactional
    def delete(DrinkPresentation drinkPresentation) {
        deleteFromFirebase drinkPresentation

        drinkPresentation.delete(flush: true)
    }
}