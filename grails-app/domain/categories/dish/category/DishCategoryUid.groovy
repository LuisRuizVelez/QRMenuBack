package categories.dish.category

import bases.BaseModel
import firebase.FBDatabase

class DishCategoryUid extends BaseModel implements Serializable {
    String uid
    FBDatabase fbDatabase

    static belongsTo = [dishCategory: DishCategory]

    static mapping = {
        id column: 'id_dish_category_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: false, blank: false
        fbDatabase nullable: false, blank: false
        dishCategory nullable: false, blank: false
    }
}
