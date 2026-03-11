package categories.drink.category

import bases.BaseModel
import catalogs.lang.Lang

class LangDrinkCategory extends BaseModel implements Serializable {
    String title
    String shortDescription
    String longDescription
    Lang lang

    static belongsTo = [drinkCategory: DrinkCategory]

    static mapping = {
        id column: 'id_lang_drink_category', generator: 'uuid'
        longDescription sqlType: 'longtext'
    }

    static constraints = {
        title nullable: false, blank: false
        lang nullable: false, blank: false
        drinkCategory nullable: false, blank: false
        shortDescription nullable: true, blank: true
        longDescription nullable: true, blank: true
    }

    def toJsonForm = {
        [
                id: id,
                title: title,
                shortDescription: shortDescription,
                longDescription: longDescription,
                lang: lang.toBasicForm()
        ]
    }

    def toFirebaseForm = {
        [
                title: title,
                shortDescription: shortDescription,
                longDescription: longDescription,
        ]
    }
}
