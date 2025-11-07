package core.menu

import core.dish.Dish
import core.drink.Drink
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

    def getDishes(String id) {
        try {
            Menu menu = Menu.get(id)

            if (menu == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            def result = menu.dishes*.toBasicForm()

            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }



    def addDish(InputData inputData){
        try {
            Menu menu = Menu.get(inputData?.item?.menu as String)
            Dish dish = Dish.get(inputData?.item?.dish as String)

            if (menu == null || dish == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            menu.addToDishes(dish)
            menu.save(flush: true, failOnError: true)

            def output = dish.toBasicForm()

            render output as JSON
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }


    def removeDish(InputData inputData){
        try {
            Menu menu = Menu.get(inputData?.item?.menu as String)
            Dish dish = Dish.get(inputData?.item?.dish as String)

            if (menu == null || dish == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            menu.removeFromDishes(dish)
            menu.save(flush: true, failOnError: true)

            render status: HttpStatus.NO_CONTENT
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }


    def getDrinks(String id) {
        try {
            Menu menu = Menu.get(id)

            if (menu == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            def result = menu.drinks*.toBasicForm()

            render result as JSON
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        }
    }


    def addDrink(InputData inputData){
        try {
            Menu menu = Menu.get(inputData?.item?.menu as String)
            def drink = Drink.get(inputData?.item?.drink as String)

            if (menu == null || drink == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            menu.addToDrinks(drink)
            menu.save(flush: true, failOnError: true)

            def output = drink.toBasicForm()

            render output as JSON
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }


    def removeDrink(InputData inputData){
        try {
            Menu menu = Menu.get(inputData?.item?.menu as String)
            Drink drink = Drink.get(inputData?.item?.drink as String)

            if (menu == null || drink == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            menu.removeFromDrinks(drink)
            menu.save(flush: true, failOnError: true)

            render status: HttpStatus.NO_CONTENT
        } catch (ValidationException ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        }
    }
}
