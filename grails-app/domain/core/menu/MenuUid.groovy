package core.menu

import bases.BaseModel
import firebase.FBDatabase

class MenuUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [menu: Menu]

    static mapping = {
        id column: 'id_menu_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        menu nullable: false, blank: false
    }
}
