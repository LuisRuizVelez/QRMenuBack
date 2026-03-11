package core.drink

import bases.BaseModel
import firebase.FBDatabase

class DrinkUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [drink: Drink]

    static mapping = {
        id column:'id_dish_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: true, blank: true
        drink nullable: true, blank: true
        fbDatabase nullable: true, blank: true
    }
}
