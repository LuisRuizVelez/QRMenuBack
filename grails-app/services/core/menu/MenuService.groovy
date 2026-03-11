package core.menu

import categories.menu.MenuMedia
import com.security.Role
import com.security.UserService
import core.dish.DishService
import core.drink.DrinkService
import firebase.StorageService
import grails.gorm.transactions.Transactional
import media.ImageMedia
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
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
        deleteFromFirebase menu

        // Desasociar y eliminar platos
        menu.dishes?.toList()?.each { dish ->
            menu.removeFromDishes(dish)
            dishService.delete(dish)
        }
        menu.dishes?.clear() // Limpiar la colección después de eliminar

        // Desasociar y eliminar bebidas
        menu.drinks?.toList()?.each { drink ->
            menu.removeFromDrinks(drink)
            drinkService.delete(drink)
        }
        menu.drinks?.clear() // Limpiar la colección después de eliminar

        // Delete associated media
        //def imagesCopy = new ArrayList<>(menu.images)
        menu?.images.toList()?.each { media ->
            storageService.deleteFile(menu, media.media.name)
            //menu.removeFromImages(media)
            media.delete(flush: true)
        }
        menu.images?.clear() // Limpiar la colección después de eliminar

        menu.delete(flush: true)
    }

    @Transactional
    def addImage(Menu menu, MultipartHttpServletRequest mpr, Boolean isThumb = false) {
        mpr?.multipartFiles?.each { key, item ->
            MultipartFile multipartFile = mpr.getFile(key)

            Map imageProperties = storageService.uploadFile menu, multipartFile, isThumb

            if(imageProperties != null){
                ImageMedia imageMedia = new ImageMedia(imageProperties)
                imageMedia.save(flush: true, failOnError: true)

                MenuMedia menuMedia = new MenuMedia(menu: menu, media: imageMedia)
                menuMedia.save(flush: true, failOnError: true)
            }
        }

        sendToFirebase menu
    }


    def removeImage(MenuMedia media) {


        storageService.deleteFile(menuMedia.media.storagePath)

        sendToFirebase menu
    }

}
