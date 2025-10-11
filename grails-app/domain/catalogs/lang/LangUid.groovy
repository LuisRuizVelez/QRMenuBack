package catalogs.lang

import bases.BaseModel
import firebase.FBDatabase

class LangUid extends BaseModel implements Serializable {
    String uid
    FBDatabase fbDatabase

    static belongsTo = [ lang: Lang ]

    static mapping = {
        id column: 'id_lang_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: false, blank: false, unique: true
        lang nullable: false
        fbDatabase nullable: true
    }
}
