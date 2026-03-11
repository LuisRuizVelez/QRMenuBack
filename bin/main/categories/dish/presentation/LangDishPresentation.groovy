package categories.dish.presentation

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangDishPresentation extends BaseModel implements Serializable {
    Lang lang
    String title

    static belongsTo = [dishPresentation: DishPresentation]

    static mapping = {
        id column: 'id_lang_dish_presentation', generator: 'uuid'
    }

    static constraints = {
        title nullable: false, blank: false
        lang nullable: false, blank: false
        dishPresentation nullable: false, blank: false
    }

    def toJsonForm = { FBDatabase fbDatabase ->
        [
                id  : id,
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
