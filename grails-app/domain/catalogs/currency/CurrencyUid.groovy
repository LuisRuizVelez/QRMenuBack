package catalogs.currency

import bases.BaseModel
import firebase.FBDatabase

class CurrencyUid extends BaseModel implements Serializable {
    FBDatabase fbDatabase
    String uid

    static belongsTo = [currency: Currency]

    static mapping = {
        id column:'id_currency_uid', generator: 'uuid'
    }

    static constraints = {
        uid nullable: true, blank: true
        currency nullable: true, blank: true
        fbDatabase nullable: true, blank: true
    }
}
