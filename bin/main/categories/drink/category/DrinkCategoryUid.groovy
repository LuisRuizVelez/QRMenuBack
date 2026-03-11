package categories.drink.category

import bases.BaseModel
import firebase.FBDatabase

class DrinkCategoryUid extends BaseModel implements Serializable {
    String uid
    FBDatabase fbDatabase

    static belongsTo = [drinkCategory: DrinkCategory]

    static mapping = {
        id column: 'id_drink_category_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: false, blank: false
        fbDatabase nullable: false, blank: false
        drinkCategory nullable: false, blank: false
    }
}
