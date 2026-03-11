package core.drink

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkUid, mainAttribute = "drink" )
@LangDomainClass( clazz = LangDrink, mainAttribute = "drink" )
class DrinkService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<Drink> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<Drink> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params= [:]) {
        Drink drink = inputData?.item as Drink

        return Drink.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (drink.drinkCategory != null)
                eq 'dishDrinkCategory' , drink.drinkCategory

            if (drink.menu != null)
                eq 'menu' , drink.menu

            if (drink.isActive != null)
                eq 'isActive' , drink.isActive

        } as List<Drink>
    }

    @Transactional
    def save(Drink _drink) {
        _drink.groupingRole = _drink.groupingRole ?: userService.getSessionValue('groupingRole') as Role
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