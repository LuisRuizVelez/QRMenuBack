package categories.dish.presentation

import bases.BaseModel
import firebase.FBDatabase

class DishPresentationUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [dishPresentation: DishPresentation]

    static mapping = {
        id column: 'id_dish_presentation_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        dishPresentation nullable: false, blank: false
    }
}
