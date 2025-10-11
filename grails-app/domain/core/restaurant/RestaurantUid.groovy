package core.restaurant

import bases.BaseModel
import firebase.FBDatabase

class RestaurantUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [restaurant: Restaurant]

    static mapping = {
        id column: 'id_restaurant_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        restaurant nullable: false, blank: false
    }
}
