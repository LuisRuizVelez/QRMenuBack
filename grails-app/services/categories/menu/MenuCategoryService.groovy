package categories.menu

import bases.BaseService
import utils.InputData
import grails.gorm.transactions.Transactional

import annotations.LangDomainClass
import annotations.UidDomainClass

@UidDomainClass( clazz = MenuCategoryUid, mainAttribute='category')
@LangDomainClass( clazz = LangMenuCategory, mainAttribute='category')
class MenuCategoryService extends BaseService {

    def search(InputData inputData, Map params) {
        MenuCategory menuCategory = inputData?.item as MenuCategory

        List<MenuCategory> result = MenuCategory.createCriteria().list(params) {
            if (menuCategory.code != null)
                ilike 'code', "%${menuCategory.code}%"

        } as List<MenuCategory>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        MenuCategory.list()*.toBasicForm()
    }

    @Transactional
    def save(MenuCategory _menuCategory) {
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