package core.menu

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class MenuController extends BaseController {

    MenuService menuService

    def search(InputData inputData){
        try {
            def result = menuService.search inputData, params as Map
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(){
        def result = menuService.getOptions()

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            Menu menu = inputData?.item as Menu
            menuService.save menu
            menu.refresh()
            def output = menu.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            Menu menu = Menu.get(inputData?.item?.id as String)
            menuService.update inputData, menu
            menu.refresh()
            def output = menu.toJsonForm inputData?.fbDatabase

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
        Menu menu = Menu.get(id)

        if (menu == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            menuService.delete menu
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
