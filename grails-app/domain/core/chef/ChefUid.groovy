package core.chef

import bases.BaseModel
import firebase.FBDatabase

class ChefUid extends BaseModel implements Serializable {
    FBDatabase database
    String uid

    static belongsTo = [chef: Chef]

    static mapping = {
        id column: 'id_chef_uid', generator: 'uuid'
    }

    static constraints = {
        database nullable: false, blank: false
        uid nullable: false, blank: false
        chef nullable: false, blank: false
    }
}
