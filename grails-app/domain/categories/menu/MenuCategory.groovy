package categories.menu

import bases.BaseModel
import com.security.Role
import firebase.FBDatabase

class MenuCategory extends BaseModel implements Serializable {
    String code
    Role groupingRole

    static hasMany = [
        langs: LangMenuCategory,
        uids: MenuCategoryUid
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
}
