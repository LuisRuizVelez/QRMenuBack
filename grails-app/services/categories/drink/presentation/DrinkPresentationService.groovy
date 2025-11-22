package categories.drink.presentation

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkPresentationUid, mainAttribute = "drinkPresentation" )
@LangDomainClass( clazz = LangDrinkPresentation, mainAttribute = "drinkPresentation" )
class DrinkPresentationService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<DrinkPresentation> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<DrinkPresentation> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params=[:]) {
        DrinkPresentation drinkPresentation = inputData?.item as DrinkPresentation

        return DrinkPresentation.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (drinkPresentation.code != null)
                ilike 'code', "%${drinkPresentation.code}%"

        } as List<DrinkPresentation>
    }

    @Transactional
    def save(DrinkPresentation _drinkPresentation) {
        _drinkPresentation.groupingRole = _drinkPresentation.groupingRole ?: userService.getSessionValue('groupingRole') as Role
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