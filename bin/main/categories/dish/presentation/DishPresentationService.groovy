package categories.dish.presentation

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishPresentationUid, mainAttribute = "dishPresentation" )
@LangDomainClass( clazz = LangDishPresentation, mainAttribute = "dishPresentation" )
class DishPresentationService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<DishPresentation> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<DishPresentation> result = filterData inputData
        result*.toBasicForm()
    }


    def filterData(InputData inputData, Map params=[:]) {
        DishPresentation presentationType = inputData?.item as DishPresentation

        return DishPresentation.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (presentationType.code != null)
                ilike 'code', "%${presentationType.code}%"

        } as List<DishPresentation>
    }

    @Transactional
    def save(DishPresentation _presentationType) {
        _presentationType.groupingRole = _presentationType.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        DishPresentation presentationType = _presentationType.save(flush: true, failOnError: true)

        if (!presentationType)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase presentationType

    }

    @Transactional
    def update(InputData inputData, DishPresentation presentationType) {
        presentationType.properties = inputData.item
        presentationType.save(flush: true, failOnError: true)

        sendToFirebase presentationType
    }

    @Transactional
    def delete(DishPresentation presentationType) {
        deleteFromFirebase presentationType

        presentationType.delete(flush: true)
    }
}
