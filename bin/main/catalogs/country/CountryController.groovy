package catalogs.country


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class CountryController extends BaseController {

    CountryService countryService

    def search(InputData inputData){
        try {
            def result = countryService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = countryService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Country country = inputData?.item as Country
            countryService.save country
            country.refresh()
            def output = country.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Country country = Country.get(inputData?.item?.id as String)
            countryService.update inputData, country
            country.refresh()
            def output = country.toJsonForm(inputData?.fbDatabase)

            render output as JSON
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def delete(String  id){
        Country country = Country.get(id)

        if (country == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            countryService.delete country
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
