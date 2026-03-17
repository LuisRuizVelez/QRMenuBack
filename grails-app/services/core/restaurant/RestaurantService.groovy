package core.restaurant

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import com.security.Role
import core.menu.MenuService
import firebase.StorageService
import media.ImageMediaService
import com.security.UserService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = RestaurantUid, mainAttribute = "restaurant" )
@LangDomainClass( clazz = LangRestaurant, mainAttribute = "restaurant" )
class RestaurantService extends BaseService {
    UserService userService
    StorageService storageService
    ImageMediaService imageMediaService
    MenuService menuService

    def search(InputData inputData, Map params) {
        List<Restaurant> result = filterData inputData, params
        [total: result?.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<Restaurant> result = filterData inputData
        result*.toBasicForm()
    }

    def filterData(InputData inputData, Map params = [:]) {
        Restaurant restaurant = inputData?.item as Restaurant

        return Restaurant.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (restaurant.code != null)
                ilike 'code', "%${restaurant.code}%"
        } as List<Restaurant>
    }

    @Transactional
    def save(Restaurant _restaurant) {
        _restaurant.groupingRole = _restaurant.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        Restaurant restaurant = _restaurant.save(flush: true, failOnError: true)

        if (!restaurant)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase restaurant
    }

    @Transactional
    def update(InputData inputData, Restaurant restaurant) {
        restaurant.properties = inputData.item
        restaurant.save(flush: true, failOnError: true)

        sendToFirebase restaurant
    }

    @Transactional
    def delete(Restaurant restaurant) {
        // Delete associated menues
        restaurant?.menues?.each { menuService.delete it }

        // Obtener los IDs de las imágenes asociadas al restaurante
        List<String> imagesIds = restaurant?.images?.collect { it?.media?.id }

        // Delete associated media
        restaurant?.images?.each { storageService.deleteFile it?.restaurant, it?.media?.name }

        // Eliminar el restaurante de Firebase
        deleteFromFirebase restaurant

        // Eliminar el restaurante de la base de datos
        restaurant.delete(flush: true)

        // Eliminar los registros de media asociados
        imageMediaService.deleteImages imagesIds
    }
}
