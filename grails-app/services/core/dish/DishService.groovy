package core.dish

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional
import org.springframework.validation.BindingResult
import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = DishUid, mainAttribute = "dish" )
@LangDomainClass( clazz = LangDish, mainAttribute = "dish" )
class DishService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<Dish> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<Dish> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params=[:]) {
        Dish dish = inputData?.item as Dish

        return Dish.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (dish.dishCategory != null)
                eq 'dishDrinkCategory' , dish.dishCategory

            if (dish.menu != null)
                eq 'menu' , dish.menu

            if (dish.isActive != null)
                eq 'isActive' , dish.isActive

        } as List<Dish>
    }

    @Transactional
    def save(Dish _dish) {
        _dish.groupingRole = _dish.groupingRole ?: userService.getSessionValue('groupingRole') as Role
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