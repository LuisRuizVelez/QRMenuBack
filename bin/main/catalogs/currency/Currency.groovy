package catalogs.currency

import bases.BaseModel
import firebase.FBDatabase

class Currency extends BaseModel implements Serializable {
    String name
    String symbol
    Boolean isActive

    static mapping = {
        id column: 'id_currency', generator: 'uuid'
    }

    static constraints = {
        name nullable: false, blank: false, unique: true
        symbol nullable: false, blank: false
        isActive nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            name: name,
            symbol: symbol,
            isActive: isActive
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
            symbol: symbol,
            isActive: isActive
        ]
    }
}
