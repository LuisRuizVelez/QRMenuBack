package categories.dish.category

import bases.BaseModel
import com.security.Role
import firebase.FBDatabase

class DishCategory extends BaseModel implements Serializable {
    String code
    String image
    Integer showOrder
    Boolean status
    Role groupingRole

    static hasMany = [
        langs: LangDishCategory,
        uids: DishCategoryUid
    ]

    static mapping = {
        id column: 'id_dish_category', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
        image nullable: true, blank: true
        showOrder nullable: true, blank: true
        status nullable: false, blank: false
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
            id: id,
            code: code,
            image: image,
            showOrder: showOrder,
            status: status,
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
            image: image,
            showOrder: showOrder,
            status: status,
        ]
    }
}
