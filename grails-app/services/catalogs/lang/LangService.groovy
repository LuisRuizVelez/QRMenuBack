package catalogs.lang

import firebase.RealtimeService
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData
import annotations.UidDomainClass


@UidDomainClass( clazz = LangUid, mainAttribute = "lang" )
class LangService extends BaseService {

    RealtimeService realtimeService

    def search(InputData inputData, Map params) {
        Lang lang = inputData?.item as Lang

        List<Lang> result = Lang.createCriteria().list(params) {
            if (lang.name != null)
                ilike 'name', "%${lang.name}%"

            if (lang.isActive != null)
                eq 'isActive', lang.isActive

            if (lang.isEnabledToEdit != null)
                eq 'isEnabledToEdit', lang.isEnabledToEdit
        } as List<Lang>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Lang.list()*.toBasicForm()
    }

    @Transactional
    def save(Lang _lang) {
        Lang lang = _lang.save(flush: true, failOnError: true)

        if (!lang)
            throw new Exception("Unable to save object")

        sendToFirebase lang

    }

    @Transactional
    def update(InputData inputData, Lang lang) {
        lang.properties = inputData.item
        lang.save(flush: true, failOnError: true)

        sendToFirebase lang
    }

    @Transactional
    def delete(Lang lang) {
        deleteFromFirebase lang
        lang.delete(flush: true)
    }
}