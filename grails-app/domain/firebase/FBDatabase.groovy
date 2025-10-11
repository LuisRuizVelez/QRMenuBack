package firebase

import bases.BaseModel

class FBDatabase extends BaseModel implements Serializable {
    String name
    String url
    Boolean isDefault
    Boolean isProduction

    static belongsTo = [
        project: FBProject
    ]

    static mapping = {
        table 'fb_database'
        id column: 'id_fb_database', generator: 'uuid'
    }

    static constraints = {
        name        nullable: true, blank: true
        url         nullable: true, blank: true
        isDefault   nullable: true, blank: true
        project     nullable: true, blank: true
        isProduction     nullable: true, blank: true
    }

    def toBasicForm = {
        [ id : id, name: name ]
    }

    def toJsonForm =  {
        [
                id: id,
                name: name,
                url: url,
                isDefault: isDefault,
                isProduction: isProduction,
                project: project?.toBasicForm()
        ]
    }
}
