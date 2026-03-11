package core.dish

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangDish extends BaseModel implements Serializable {
    Lang lang
    String title
    String shortDescription
    String longDescription

    static belongsTo = [dish: Dish]

    static mapping = {
        id column:'id_lang_dish', generator: 'uuid'
        longDescription sqlType: 'longtext'
    }

    static constraints = {
        title nullable: true, blank: true
        shortDescription nullable: true, blank: true
        longDescription nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            lang: lang?.toBasicForm(),
            title: title,
            shortDescription: shortDescription,
            longDescription: longDescription
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null  ->
        [
            lang: lang?.toBasicForm(),
            title: title,
            shortDescription: shortDescription,
            longDescription: longDescription
        ]
    }
}
