package categories.drink.presentation

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangDrinkPresentation extends BaseModel implements Serializable {
    Lang lang
    String title

    static belongsTo = [drinkPresentation: DrinkPresentation]

    static mapping = {
        id column: 'id_lang_drink_presentation', generator: 'uuid'
    }

    static constraints = {
        title nullable: false, blank: false
        lang nullable: false, blank: false
        drinkPresentation nullable: false, blank: false
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
