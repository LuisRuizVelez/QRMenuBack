package catalogs.currency


import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class CurrencyController extends BaseController {

    CurrencyService currencyService

    def search(InputData inputData){
        try {
            def result = currencyService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = currencyService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Currency currency = inputData?.item as Currency
            currencyService.save currency
            currency.refresh()
            def output = currency.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Currency currency = Currency.get(inputData?.item?.id as String)
            currencyService.update inputData, currency
            currency.refresh()
            def output = currency.toJsonForm inputData?.fbDatabase

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
        Currency currency = Currency.get(id)

        if (currency == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            currencyService.delete currency
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
