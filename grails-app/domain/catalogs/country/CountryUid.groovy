package catalogs.country

import bases.BaseModel
import firebase.FBDatabase

class CountryUid extends BaseModel implements Serializable {
    String uid
    FBDatabase fbDatabase

    static belongsTo = [ country: Country ]

    static mapping = {
        id column: 'id_country_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: false, blank: false, unique: true
        country nullable: false, blank: false
        fbDatabase nullable: false, blank: false
    }
}
