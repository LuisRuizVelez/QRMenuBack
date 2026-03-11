package core.chef

import bases.BaseModel
import catalogs.lang.Lang

class LangChef extends BaseModel implements Serializable {
    Lang lang
    String shortDescription
    String longDescription

    static belongsTo = [chef: Chef]

    static mapping = {
        id column: 'id_lang_restaurant', generator: 'uuid'
    }

    static constraints = {
        lang nullable: false, blank: false
        chef nullable: false, blank: false
        shortDescription nullable: true, blank: true
        longDescription nullable: true, blank: true
    }

    def toJsonForm = {
        [
                id: id,
                shortDescription: shortDescription,
                longDescription: longDescription,
                lang: lang?.toBasicForm(),
                chef: chef?.toBasicForm(),
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def output = [:]

        if (isValidString(shortDescription))
            output.put("shortDescription", shortDescription)

        if (isValidString(longDescription))
            output.put("longDescription", longDescription)


        return output
    }

    def notTranslatableProperties = {
        [:]
    }
}