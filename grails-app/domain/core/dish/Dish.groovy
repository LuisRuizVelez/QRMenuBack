package core.dish

import core.menu.Menu
import bases.BaseModel
import com.security.Role
import firebase.FBDatabase
import categories.dish.category.DishCategory

class Dish extends BaseModel implements Serializable {
    DishCategory dishCategory
    String image
    Integer showOrder
    Boolean isActive
    Role groupingRole

    static belongsTo = [menu: Menu]

    static hasMany = [
            langs: LangDish,
            uids: DishUid,
            prices: DishPrice
    ]

    static mapping = {
        id column:'id_dish', generator: 'uuid'
    }

    static constraints = {
        image nullable: true, blank: true
        showOrder nullable: true, blank: true
        isActive nullable: false, blank: true
        dishCategory nullable: true, blank: true
        menu nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            image: image,
            title: getDefaultLangProperty(langs, 'title'),
            description: getDefaultLangProperty(langs, 'shortDescription'),
            showOrder: showOrder,
            isActive: isActive,
            menu: menu?.toBasicForm(),
            dishCategory: dishCategory?.toBasicForm(),
            langs: langs*.toJsonForm()
        ]
    }

    def toBasicForm = {
        [
            id: id,
            title: getDefaultLangProperty(langs, 'title')
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null  ->
        [
            category : dishCategory?.getUidProperty(dishCategory?.uids, fbDatabase),
            image:image,
            showOrder:showOrder,
            isActive:isActive ? 1 : 0
        ]
    }
}
