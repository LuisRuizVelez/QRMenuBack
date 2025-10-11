package categories.restaurant

import bases.BaseModel
import firebase.FBDatabase

class RestaurantCategoryUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [restaurantCategory: RestaurantCategory]

    static mapping = {
        id column: 'id_category_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        restaurantCategory nullable: false, blank: false
    }
}
