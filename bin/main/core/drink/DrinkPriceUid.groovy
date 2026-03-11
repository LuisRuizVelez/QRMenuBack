package core.drink

import bases.BaseModel
import firebase.FBDatabase

class DrinkPriceUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [drinkPrice: DrinkPrice]

    static mapping = {
        id column: 'id_drink_price_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        drinkPrice nullable: false, blank: false
    }
}
