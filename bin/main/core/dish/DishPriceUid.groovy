package core.dish

import bases.BaseModel
import firebase.FBDatabase

class DishPriceUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [dishPrice: DishPrice]

    static mapping = {
        id column: 'id_drink_price_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        dishPrice nullable: false, blank: false
    }
}
