package categories.drink.presentation

import bases.BaseModel
import firebase.FBDatabase

class DrinkPresentationUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [drinkPresentation: DrinkPresentation]

    static mapping = {
        id column: 'id_drink_presentation_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        drinkPresentation nullable: false, blank: false
    }
}
