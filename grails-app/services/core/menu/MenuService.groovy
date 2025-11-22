package core.menu

import com.security.Role
import com.security.UserService
import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = MenuUid, mainAttribute = "menu" )
@LangDomainClass( clazz = LangMenu, mainAttribute = "menu" )
class MenuService extends BaseService {
    UserService userService

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

        menu.delete(flush: true)
    }
}
