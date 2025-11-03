package catalogs.currency

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.UidDomainClass

@UidDomainClass( clazz = CurrencyUid, mainAttribute = "currency" )
class CurrencyService extends BaseService {

    def search(InputData inputData, Map params) {
        Currency currency = inputData?.item as Currency

        List<Currency> result = Currency.createCriteria().list(params) {
            if (currency.name != null)
                ilike 'name', "%${currency.name}%"

        } as List<Currency>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Currency.list()*.toBasicForm()
    }

    @Transactional
    def save(Currency _currency) {
        Currency currency = _currency.save(flush: true, failOnError: true)

        if (!currency)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase currency
    }

    @Transactional
    def update(InputData inputData, Currency currency) {
        currency.properties = inputData.item
        currency.save(flush: true, failOnError: true)

        sendToFirebase currency
    }

    @Transactional
    def delete(Currency currency) {
        deleteFromFirebase currency

        currency.delete(flush: true)
    }
}
