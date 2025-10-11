package core.dish

import grails.gorm.transactions.Transactional
import org.springframework.validation.BindingResult
import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishUid, mainAttribute = "dish" )
@LangDomainClass( clazz = LangDish, mainAttribute = "dish" )
class DishService extends BaseService {

    def search(InputData inputData, Map params) {
        Dish dish = inputData?.item as Dish

        List<Dish> result = Dish.createCriteria().list(params) {
            if (dish.dishCategory != null)
               eq 'dishDrinkCategory' , dish.dishCategory

            if (dish.menu != null)
                eq 'menu' , dish.menu

            if (dish.isActive != null)
                eq 'isActive' , dish.isActive

        } as List<Dish>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Dish.list()*.toBasicForm()
    }

    @Transactional
    def save(Dish _dish) {
        Dish dish = _dish.save(flush: true, failOnError: true)

        if (!dish)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase dish
    }

    @Transactional
    def update(InputData inputData, Dish dish) {
        dish.properties = inputData.item as BindingResult
        dish.save(flush: true, failOnError: true)

        sendToFirebase dish
    }

    @Transactional
    def delete(Dish dish) {
        deleteFromFirebase dish

        dish.delete(flush: true)
    }
}