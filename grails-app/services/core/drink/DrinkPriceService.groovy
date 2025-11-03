package core.drink

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.UidDomainClass

@UidDomainClass( clazz = DrinkPriceUid, mainAttribute = "drinkPrice" )
class DrinkPriceService extends BaseService {

    def search(InputData inputData, Map params) {

        List<DrinkPrice> result = DrinkPrice.createCriteria().list(params) {
            drink {
                eq('id', inputData?.item?.id)
            }

        } as List<DrinkPrice>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DrinkPrice.list()*.toBasicForm()
    }

    @Transactional
    def save(DrinkPrice _drinkPrice) {
        DrinkPrice drinkPrice = _drinkPrice.save(flush: true, failOnError: true)

        if (!drinkPrice)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase drinkPrice
    }

    @Transactional
    def update(InputData inputData, DrinkPrice drinkPrice) {
        drinkPrice.properties = inputData.item
        drinkPrice.save(flush: true, failOnError: true)

        sendToFirebase drinkPrice
    }

    @Transactional
    def delete(DrinkPrice drinkPrice) {
        try {
            if (drinkPrice.drink)
                drinkPrice.drink.removeFromPrices(drinkPrice) // Elimina la referencia en Drink


            deleteFromFirebase drinkPrice

            drinkPrice.delete(flush: true) // Elimina el objeto DrinkPrice
        } catch (Exception ex) {
            log.error("Error al eliminar DrinkPrice: ${ex.message}", ex)
            throw new Exception("No se pudo eliminar el objeto DrinkPrice. Detalles: ${ex.message}")
        }
    }
}
