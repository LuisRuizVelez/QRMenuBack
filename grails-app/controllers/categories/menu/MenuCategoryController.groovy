package categories.menu

import grails.converters.JSON
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional

import bases.BaseController
import utils.InputData

class MenuCategoryController extends BaseController {

    MenuCategoryService menuCategoryService

    def search(InputData inputData){
        try {
            def result = menuCategoryService.search(inputData, params as Map)
            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def getOptions(InputData inputData){
        def result = menuCategoryService.getOptions inputData

        render result as JSON
    }


    @Transactional
    def save(InputData inputData) {
        try {
            MenuCategory menuCategory = inputData?.item as MenuCategory
            menuCategoryService.save menuCategory
            menuCategory.refresh()
            def output = menuCategory.toJsonForm inputData?.fbDatabase

            render output as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }

    @Transactional
    def update(InputData inputData) {
        try {
            MenuCategory menuCategory = MenuCategory.get(inputData?.item?.id as String)
            menuCategoryService.update inputData, menuCategory
            menuCategory.refresh()
            def output = menuCategory.toJsonForm inputData?.fbDatabase

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
        MenuCategory menuCategory = MenuCategory.get(id)

        if (menuCategory == null) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            menuCategoryService.delete menuCategory
        } catch (Exception ex) {
            ex.printStackTrace()
            respond status: HttpStatus.INTERNAL_SERVER_ERROR
            return
        }

        render status: HttpStatus.NO_CONTENT
    }
}
