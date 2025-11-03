package catalogs.currency

import bases.BaseModel
import firebase.FBDatabase

class Currency extends BaseModel implements Serializable {
    String name
    String symbol

    static mapping = {
        id column: 'id_currency', generator: 'uuid'
    }

    static constraints = {
        name nullable: false, blank: false, unique: true
        symbol nullable: false, blank: false
    }

    def toJsonForm = {
        [
            id: id,
            name: name,
            symbol: symbol,
        ]
    }

    def toBasicForm = {
        [
            id: id,
            name: name
        ]
    }

    def toFirebaseForm = {  FBDatabase fbDatabase = null ->
        [
            name: name,
            symbol: symbol
        ]
    }
}
