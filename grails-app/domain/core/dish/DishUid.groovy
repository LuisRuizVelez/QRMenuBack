package core.dish

import bases.BaseModel
import firebase.FBDatabase

class DishUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [dish: Dish]

    static mapping = {
        id column:'id_dish_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: true, blank: true
        dish nullable: true, blank: true
        fbDatabase nullable: true, blank: true
    }
}
