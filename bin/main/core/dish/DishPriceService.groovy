package core.dish

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.UidDomainClass

@UidDomainClass( clazz = DishPriceUid, mainAttribute = "dishPrice" )
class DishPriceService extends BaseService {

    def search(InputData inputData, Map params) {
        List<DishPrice> result = DishPrice.createCriteria().list(params) {
            dish {
                eq('id', inputData?.item?.id)
            }

        } as List<DishPrice>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        DishPrice.list()*.toBasicForm()
    }

    @Transactional
    def save(DishPrice _dishPrice) {
        DishPrice dishPrice = _dishPrice.save(flush: true, failOnError: true)

        if (!dishPrice)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase dishPrice
    }

    @Transactional
    def update(InputData inputData, DishPrice dishPrice) {
        dishPrice.properties = inputData.item
        dishPrice.save(flush: true, failOnError: true)

        sendToFirebase dishPrice
    }

    @Transactional
    def delete(DishPrice dishPrice) {
        try {
            if (dishPrice.dish)
                dishPrice.dish.removeFromPrices(dishPrice) // Elimina la referencia en Dish

            deleteFromFirebase dishPrice

            dishPrice.delete(flush: true)
        } catch (Exception ex) {
            log.error("Error al eliminar DishPrice: ${ex.message}", ex)
            throw new Exception("No se pudo eliminar el objeto DishPrice. Detalles: ${ex.message}")
        }
    }
}
