package catalogs.country

import bases.BaseModel
import firebase.FBDatabase

class Country extends BaseModel implements Serializable {
    String code
    String name

    static hasMany = [
        uids: CountryUid
    ]

    static mapping = {
        id column: 'id_country', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false, unique: true
        name nullable: false, blank: false
    }

    def toJsonForm = {
        [
            id: id,
            code: code,
            name: name
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null ->
        [
            code: code,
            name: name
        ]
    }

    def toBasicForm = {
        [
            id: id,
            code: code,
            name: name
        ]
    }
}
