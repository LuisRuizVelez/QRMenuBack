package core.menu

import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = MenuUid, mainAttribute = "menu" )
@LangDomainClass( clazz = LangMenu, mainAttribute = "menu" )
class MenuService extends BaseService {

    def search(InputData inputData, Map params) {
        Menu menu = inputData?.item as Menu

        List<Menu> result = Menu.createCriteria().list(params) {
            if (menu.code != null)
                ilike 'code', "%${menu.code}%"

        } as List<Menu>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Menu.list()*.toBasicForm()
    }

    @Transactional
    def save(Menu _menu) {
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
