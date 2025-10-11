package catalogs.lang

import bases.BaseModel
import firebase.FBDatabase

class Lang extends BaseModel implements Serializable{
    String name
    String twoLetterCode
    String threeLetterCode
    Boolean isEnabledToEdit
    Boolean isDefault
    Boolean isBaseTranslate
    Boolean isActive

    static hasMany = [ uids:LangUid ]

    static mapping = {
        id column: 'id_lang', generator: 'uuid'
    }

    static constraints = {
        name nullable: true, blank: true
        twoLetterCode nullable: true, blank: true, unique: true, maxSize: 2
        threeLetterCode nullable: true, blank: true, unique: true, maxSize: 3
        isDefault nullable: true, blank: true
        isBaseTranslate nullable: true, blank: true
        isEnabledToEdit nullable: true, blank: true
        isActive nullable: true, blank: true
    }

    def toJsonForm = {
        [
                id              : id,
                twoLetterCode   : twoLetterCode,
                threeLetterCode : threeLetterCode,
                name            : name,
                isDefault       : isDefault,
                isBaseTranslate : isBaseTranslate,
                isEnabledToEdit : isEnabledToEdit,
                isActive        : isActive
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase  = null ->
        [
                twoLetterCode   : twoLetterCode,
                threeLetterCode : threeLetterCode,
                name            : name,
                isDefault       : isDefault,
                isBaseTranslate : isBaseTranslate,
                isEnabledToEdit : isEnabledToEdit,
                isActive        : isActive
        ]
    }

    def toBasicForm = {
        [
                id              : id,
                twoLetterCode   : twoLetterCode,
                name            : name
        ]
    }
}
