package core.menu

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangMenu extends BaseModel implements Serializable {
    Lang lang
    String title
    String shortDescription
    String longDescription

    static belongsTo = [menu: Menu]

    static mapping = {
        id column: 'id_lang_menu', generator: 'uuid'
        longDescription sqlType: "longtext"
    }

    static constraints = {
        lang nullable: false, blank: false
        menu nullable: false, blank: false
        title nullable: false, blank: false
        shortDescription nullable: true, blank: true
        longDescription nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            lang: lang?.toBasicForm(),
            menu: menu?.toBasicForm(),
            title: title,
            shortDescription: shortDescription,
            longDescription: longDescription,
        ]
    }

    def toBasicForm = {
        [
            id: id,
            title: title,
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null  ->
        [
            title: title,
            shortDescription: shortDescription,
            longDescription: longDescription,
        ]
    }
}
