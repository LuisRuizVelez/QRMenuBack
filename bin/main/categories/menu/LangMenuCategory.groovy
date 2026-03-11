package categories.menu

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangMenuCategory extends BaseModel implements Serializable {
    Lang lang
    String title

    static belongsTo = [category: MenuCategory]

    static mapping = {
        id column: 'id_lang_category', generator: 'uuid'
    }

    static constraints = {
        lang nullable: false, blank: false
        category nullable: false, blank: false
        title nullable: false, blank: false
    }

    def toJsonForm = {
        [
            id: id,
            lang: lang?.toBasicForm(),
            category: category?.toBasicForm(),
            title: title,
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null ->
        [
            title: title
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def output = [:]

        if (isValidString(title))
            output.put("title", title)

        return output
    }

    def notTranslatableProperties = {
        [:]
    }
}
