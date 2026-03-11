package categories.menu

import bases.BaseService
import com.security.Role
import com.security.UserService
import utils.InputData
import grails.gorm.transactions.Transactional

import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = MenuCategoryUid, mainAttribute='category')
@LangDomainClass( clazz = LangMenuCategory, mainAttribute='category')
class MenuCategoryService extends BaseService {
    UserService userService

    def search(InputData inputData, Map params) {
        List<MenuCategory> result = filterData inputData, params
        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions(InputData inputData) {
        List<MenuCategory> result = filterData inputData
        result*.toBasicForm()
    }


    def filterData(InputData inputData, Map params=[:]) {
        MenuCategory menuCategory = inputData?.item as MenuCategory

        return MenuCategory.createCriteria().list(params) {
            if (userService.getSessionValue('enabledFilterByRole') == true)
                eq 'groupingRole', userService.getSessionValue('groupingRole')

            if (menuCategory.code != null)
                ilike 'code', "%${menuCategory.code}%"

        } as List<MenuCategory>
    }


    @Transactional
    def save(MenuCategory _menuCategory) {
        _menuCategory.groupingRole = _menuCategory.groupingRole ?: userService.getSessionValue('groupingRole') as Role
        MenuCategory menuCategory = _menuCategory.save(flush: true, failOnError: true)

        if (!menuCategory)
            throw new Exception("Unable to save object.")

        sendToFirebase menuCategory
    }

    @Transactional
    def update(InputData inputData, MenuCategory menuCategory) {
        menuCategory.properties = inputData.item
        menuCategory.save(flush: true, failOnError: true)

        sendToFirebase menuCategory
    }

    @Transactional
    def delete(MenuCategory menuCategory) {
        deleteFromFirebase menuCategory
        menuCategory.delete(flush: true)
    }
}