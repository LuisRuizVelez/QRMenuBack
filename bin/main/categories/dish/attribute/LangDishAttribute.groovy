package categories.dish.attribute

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangDishAttribute extends BaseModel implements Serializable {
    Lang lang
    String title

    static belongsTo = [ dishAttribute: DishAttribute ]

    static mapping = {
        id column: 'id_lang_dish_attribute', generator: 'uuid'
    }

    static constraints = {
        title nullable: false, blank: false
        lang nullable: false, blank: false
        dishAttribute nullable: false, blank: false
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
            id: id,
            title: title,
            lang: lang.toBasicForm()

        ]
    }

    def toFirebaseForm = {
        [
            title: title,
        ]
    }
}