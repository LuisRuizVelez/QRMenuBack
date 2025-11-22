package categories.dish.attribute

import bases.BaseModel
import com.security.Role
import firebase.FBDatabase


class DishAttribute extends BaseModel implements Serializable {
    String code
    String icon
    String color
    String relatedImage
    Role groupingRole

    static hasMany = [
        langs: LangDishAttribute,
        uids: DishAttributeUid
    ]

    static mapping = {
        id column: 'id_dish_attribute', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
        icon nullable: true, blank: true
        color nullable: true, blank: true
        relatedImage nullable: true, blank: true
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
            id: id,
            code: code,
            icon: icon,
            color: color,
            relatedImage: relatedImage,
            title: getDefaultLangProperty(langs, 'title'),
            langs: langs*.toJsonForm(fbDatabase)
        ]
    }

    def toBasicForm = {
        [
            id  : id,
            code: code,
            title: getDefaultLangProperty(langs, 'title'),
        ]
    }

    def toFirebaseForm = {
        [
            code: code,
            icon: icon,
            color: color,
            relatedImage: relatedImage,
        ]
    }
}
