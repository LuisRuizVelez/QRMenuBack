package categories.dish.presentation

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishPresentationUid, mainAttribute = "dishPresentation" )
@LangDomainClass( clazz = LangDishPresentation, mainAttribute = "dishPresentation" )
class DishPresentationService extends BaseService {

    def search(InputData inputData, Map params) {
        DishPresentation presentationType = inputData?.item as DishPresentation

        List<DishPresentation> result = DishPresentation.createCriteria().list(params) {
            if (presentationType.code != null)
                ilike 'code', "%${presentationType.code}%"

        } as List<DishPresentation>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DishPresentation.list()*.toBasicForm()
    }

    @Transactional
    def save(DishPresentation _presentationType) {
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
