package firebase

import bases.BaseModel

class FBChildPath extends BaseModel implements Serializable {
    String componentName
    String url
    FBDatabase fbDatabase

    static mapping = {
        table 'fb_child_path'
        id column: 'id_child', generator: 'uuid'
    }

    static constraints = {
        componentName nullable: false, blank: false
        url nullable: false, blank: false
        fbDatabase nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            componentName: componentName,
            url: url,
            fbDatabase: fbDatabase?.toBasicForm()
        ]
    }


    def getProjectName = {
        return fbDatabase?.project?.name
    }

    def getDBUrl = {
        return fbDatabase?.url
    }

    def getPath = { String langCode = null ->
        if (langCode)
            return url.replace('{langCode}', langCode)

        return url
    }

    def urlToFirestore = {
        if (url == null || url.isEmpty())
            throw new Exception("The path cannot be empty.")

        if(url.charAt(0) == '/')
            return url.substring(1)

        return url
    }
}
