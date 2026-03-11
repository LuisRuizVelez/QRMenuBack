package core.chef

import bases.BaseModel

class Chef extends BaseModel implements Serializable {
    String code
    String name
    String image
    String videoUrl
    Boolean status

    static hasMany = [langs: LangChef]

    static mapping = {
        id column: 'id_restaurant', generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false
        name nullable: false, blank: false
    }

    def toJsonForm = {
        [
                id   : id,
                code : code,
                name: name,
                image: image,
                videoUrl: videoUrl,
                status: status,
                langs: langs*.toJsonForm()
        ]
    }

    def toBasicForm = {
        [
                id  : id,
                name: name
        ]
    }

    def getObjectToTranslate = { String appCode ->
        def lang = langs.find { it?.lang?.isBaseTranslate }
        return lang?.getObjectToTranslate(appCode)
    }
}