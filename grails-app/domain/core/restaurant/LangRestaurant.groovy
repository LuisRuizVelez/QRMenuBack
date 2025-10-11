package core.restaurant

import bases.BaseModel
import catalogs.lang.Lang
import firebase.FBDatabase

class LangRestaurant extends BaseModel implements Serializable {
    Lang lang
    String name
    String videoUrl
    String shortDescription
    String longDescription

    static belongsTo = [restaurant: Restaurant]

    static mapping = {
        id column: 'id_lang_restaurant', generator: 'uuid'
        longDescription sqlType: "longtext"
    }

    static constraints = {
        lang nullable: false, blank: false
        restaurant nullable: false, blank: false
        name nullable: false, blank: false
        videoUrl nullable: true, blank: true
        longDescription nullable: true, blank: true
        shortDescription nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            lang: lang?.toBasicForm(),
            restaurant: restaurant?.toBasicForm(),
            name: name,
            videoUrl: videoUrl,
            shortDescription: shortDescription,
            longDescription: longDescription,
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase ->
        [
            name: name,
            videoUrl: videoUrl,
            shortDescription: shortDescription,
            longDescription: longDescription,
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def output = [:]

        if (isValidString(name))
            output.put("name", name)

        return output
    }

    def notTranslatableProperties = {
        [:]
    }
}
