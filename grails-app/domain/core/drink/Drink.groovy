package core.drink

import bases.BaseModel
import categories.drink.category.DrinkCategory
import core.menu.Menu
import firebase.FBDatabase

class Drink extends BaseModel implements Serializable {
    DrinkCategory drinkCategory
    String image
    Integer showOrder
    Boolean isActive

    static belongsTo = [menu: Menu]

    static hasMany = [
            langs: LangDrink,
            uids: DrinkUid,
            prices: DrinkPrice
    ]

    static mapping = {
        id column:'id_dish', generator: 'uuid'
    }

    static constraints = {
        image nullable: true, blank: true
        showOrder nullable: true, blank: true
        isActive nullable: false, blank: true
        drinkCategory nullable: true, blank: true
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
                drinkCategory: drinkCategory?.toBasicForm(),
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
                category : drinkCategory?.getUidProperty(drinkCategory?.uids, fbDatabase),
                image:image,
                showOrder:showOrder,
                isActive:isActive ? 1 : 0
        ]
    }

}
