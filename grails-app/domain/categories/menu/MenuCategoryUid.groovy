package categories.menu

import bases.BaseModel
import firebase.FBDatabase

class MenuCategoryUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [category: MenuCategory]

    static mapping = {
        id column: 'id_category_uid', generator: 'uuid'
    }

    static constraints = {
        fbDatabase nullable: false, blank: false
        uid nullable: false, blank: false
        category nullable: false, blank: false
    }
}
