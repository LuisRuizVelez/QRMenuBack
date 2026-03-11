package catalogs.country

import annotations.UidDomainClass
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData


@UidDomainClass( clazz = CountryUid, mainAttribute = "country" )
class CountryService extends BaseService {

    def search(InputData inputData, Map params) {
        Country country = inputData?.item as Country

        List<Country> result = Country.createCriteria().list(params) {
            if (country.name != null)
                ilike 'name', "%${country.name}%"

            if (country.code != null)
                ilike 'code', "%${country.code}%"

        } as List<Country>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Country.list()*.toBasicForm()
    }

    @Transactional
    def save(Country _country) {
        Country country = _country.save(flush: true, failOnError: true)

        if (!country)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase(country)

    }

    @Transactional
    def update(InputData inputData, Country country) {
        country.properties = inputData.item
        country.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(Country country) {
        deleteFromFirebase(country)
        country.delete(flush: true)
    }
}
