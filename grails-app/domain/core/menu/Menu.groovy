package core.menu

import core.dish.Dish
import core.drink.Drink
import core.restaurant.Restaurant
import categories.menu.MenuCategory
import categories.menu.MenuMedia
import bases.BaseModel
import com.security.Role
import firebase.FBDatabase

class Menu extends BaseModel implements Serializable {
    String code
    Integer showOrder
    Boolean isActive
    MenuCategory category
    Role groupingRole

    static belongsTo = [restaurant: Restaurant]

    static hasMany = [
            langs: LangMenu,
            uids: MenuUid,
            dishes: Dish,
            drinks: Drink,
            images: MenuMedia
    ]

    static mapping = {
        id column: 'id_menu', generator: 'uuid'
    }

    static constraints = {
        code        nullable: false, blank: false
        isActive    nullable: false, blank: false
        restaurant  nullable: false, blank: false
        category    nullable: false, blank: false
        showOrder   nullable: true, blank: true
    }

    def toJsonForm = { FBDatabase fbDatabase = null  ->
        [
            id: id,
            code: code,
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
            showOrder: showOrder,
            isActive: isActive ? 1 : 0,
            restaurant: restaurant?.getUidProperty(restaurant?.uids, fbDatabase),
            category: category?.getUidProperty(category?.uids, fbDatabase),
            images: images?.collect { it.toFirebaseForm() }
        ]
    }
}
