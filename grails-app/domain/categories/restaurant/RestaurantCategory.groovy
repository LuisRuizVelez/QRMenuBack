package categories.restaurant

import bases.BaseModel
import firebase.FBDatabase

class RestaurantCategory extends BaseModel implements Serializable {
    String code

    static hasMany = [
            langs: LangRestaurantCategory,
            uids: RestaurantCategoryUid
    ]

    static mapping = {
        id column: 'id_category', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
                id: id,
                code: code,
                title: getDefaultLangProperty(langs, 'title'),
                langs: langs*.toJsonForm()
        ]
    }

    def toBasicForm = {
        [
                id  : id,
                code: code,
                name: getDefaultLangProperty(langs, 'title'),
        ]
    }

    def toFirebaseForm = {
        [
                code: code,
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def lang = langs.find{ it?.lang?.isBaseTranslate }
        return lang?.getObjectToTranslate(appCode)
    }
}
