package categories.dish.attribute

import bases.BaseModel
import firebase.FBDatabase

class DishAttributeUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [dishAttribute: DishAttribute]

    static mapping = {
        id column: 'id_dish_attribute_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        dishAttribute nullable: false, blank: false
    }
}