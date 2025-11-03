package core.drink

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkUid, mainAttribute = "drink" )
@LangDomainClass( clazz = LangDrink, mainAttribute = "drink" )
class DrinkService extends BaseService {

    def search(InputData inputData, Map params) {
        Drink drink = inputData?.item as Drink

        List<Drink> result = Drink.createCriteria().list(params) {
            if (drink.drinkCategory != null)
                eq 'dishDrinkCategory' , drink.drinkCategory

            if (drink.menu != null)
                eq 'menu' , drink.menu

            if (drink.isActive != null)
                eq 'isActive' , drink.isActive

        } as List<Drink>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Drink.list()*.toBasicForm()
    }

    @Transactional
    def save(Drink _drink) {
        Drink drink = _drink.save(flush: true, failOnError: true)

        if (!drink)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase drink
    }

    @Transactional
    def update(InputData inputData, Drink drink) {
        drink.properties = inputData.item
        drink.save(flush: true, failOnError: true)

        sendToFirebase drink
    }

    @Transactional
    def delete(Drink drink) {
        deleteFromFirebase drink

        drink.delete(flush: true)
    }
}