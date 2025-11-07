package core.menu

import bases.BaseModel
import categories.menu.MenuCategory
import core.dish.Dish
import core.drink.Drink
import core.restaurant.Restaurant
import firebase.FBDatabase

class Menu extends BaseModel implements Serializable {
    String code
    String image
    Integer showOrder
    Boolean isActive
    MenuCategory category

    static belongsTo = [restaurant: Restaurant]

    static hasMany = [
            langs: LangMenu,
            uids: MenuUid,
            dishes: Dish,
            drinks: Drink
    ]

    static mapping = {
        id column: 'id_menu', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
        image nullable: true, blank: true
        showOrder nullable: true, blank: true
        isActive nullable: false, blank: false
        restaurant nullable: false, blank: false
        category nullable: false, blank: false
    }

    def toJsonForm = {
        [
            id: id,
            code: code,
            image: image,
            title: getDefaultLangProperty(langs, 'title'),
            showOrder: showOrder,
            isActive: isActive,
            restaurant: restaurant?.toBasicForm(),
            langs: langs*.toJsonForm(),
            category: category?.toBasicForm()
        ]
    }

    def toBasicForm = {
        [
            id: id,
            code: code,
            name: getDefaultLangProperty(langs, 'title')
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null  ->
        [
            code: code,
            image: image,
            showOrder: showOrder,
            isActive: isActive ? 1 : 0,
            restaurant: restaurant.getUidProperty(restaurant?.uids, fbDatabase),
            category: category?.getUidProperty(category?.uids, fbDatabase),
        ]
    }
}
