package categories.drink.presentation

import bases.BaseModel
import firebase.FBDatabase


class DrinkPresentation extends BaseModel implements Serializable {
    String code

    static hasMany = [
            langs: LangDrinkPresentation,
            uids: DrinkPresentationUid
    ]

    static mapping = {
        id colum: 'id_drink_presentation', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
    }

    def toJsonForm = { FBDatabase fbDatabase = null ->
        [
                id: id,
                code: code,
                title: getDefaultLangProperty(langs, 'title'),
                langs: langs*.toJsonForm(fbDatabase)
        ]
    }

    def toBasicForm = {
        [
                id  : id,
                code: code,
                title: getDefaultLangProperty(langs, 'title'),
        ]
    }

    def toFirebaseForm = {
        [
                code: code,
        ]
    }
}
