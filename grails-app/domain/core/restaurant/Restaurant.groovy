package core.restaurant

import core.menu.Menu
import bases.BaseModel
import com.security.Role
import firebase.FBDatabase
import categories.restaurant.RestaurantCategory

class Restaurant extends BaseModel implements Serializable {
    String code
    Boolean needReservation = false
    Boolean isActive = true
    RestaurantCategory category
    Role groupingRole

    static hasMany = [
            langs: LangRestaurant,
            uids: RestaurantUid,
            menues: Menu
    ]

    static mapping = {
        id column: 'id_restaurant', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
        needReservation nullable: false, blank: false
        isActive nullable: false, blank: false
        category nullable: true, blank: true
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
            id: id,
            code: code,
            name: getDefaultLangProperty(langs, "name"),
            needReservation: needReservation,
            isActive: isActive,
            category: category?.toBasicForm(),
            langs: langs*.toJsonForm()
        ]
    }

    def toBasicForm = {
        [
            id: id,
            code: code,
            name: getDefaultLangProperty(langs, "name")
        ]
    }


    def toFirebaseForm = { FBDatabase fbDatabase = null ->
        [
            code: code,
            name: getDefaultLangProperty(langs, "name"),
            needReservation: needReservation ? 1 : 0,
            isActive: isActive ? 1 : 0,
            category: category?.getUidProperty(category?.uids, fbDatabase) ?: null
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def lang = langs.find { it?.lang?.isBaseTranslate }
        return lang?.getObjectToTranslate(appCode)
    }
}
