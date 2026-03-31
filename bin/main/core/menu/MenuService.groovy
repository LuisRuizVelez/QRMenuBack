package core.menu

import com.security.Role
import com.security.UserService
import core.dish.DishService
import core.drink.DrinkService
import firebase.StorageService
import grails.gorm.transactions.Transactional
import media.ImageMediaService
import utils.InputData
import bases.BaseService

import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = MenuUid, mainAttribute = "menu" )
@LangDomainClass( clazz = LangMenu, mainAttribute = "menu" )
class MenuService extends BaseService {
    UserService userService
    StorageService storageService
    DishService dishService
    DrinkService drinkService
    ImageMediaService imageMediaService

    def search(InputData inputData, Map params) {
        List<Menu> result = filterData inputData, params
        [total: result?.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<Menu> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params = [:]) {
        Menu menu = inputData?.item as Menu

        return Menu.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (menu.code != null)
                ilike 'code', "%${menu?.code}%"

        } as List<Menu>
    }

    @Transactional
    def save(Menu _menu) {
        _menu.groupingRole = _menu.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        Menu menu = _menu.save(flush: true, failOnError: true)

        if (!menu)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase menu
    }

    @Transactional
    def update(InputData inputData, Menu menu) {
        menu.properties = inputData.item
        menu.save(flush: true, failOnError: true)

        sendToFirebase menu
    }

    @Transactional
    def delete(Menu menu) {
        menu.restaurant?.removeFromMenues(menu) // Elimina la referencia en Restaurant

        // Desasociar y eliminar platos
        menu?.dishes?.each { dish -> dishService.delete(dish) }

        // Desasociar y eliminar bebidas
        menu?.drinks?.each { drink -> drinkService.delete(drink) }

        List<String> imagesIds = menu?.images?.collect { media -> media?.media?.id }

        // Delete associated media
        menu?.images?.each { storageService.deleteFile it?.menu, it?.media?.name }

        // Delete menu from Firebase
        deleteFromFirebase menu

        // Eliminar el menú
        menu.delete(flush: true)

        // Eliminar los registros de media asociados
        imageMediaService.deleteImages imagesIds
    }
}
