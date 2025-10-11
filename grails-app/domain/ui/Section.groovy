package ui

import bases.BaseModel

class Section extends BaseModel implements Serializable{
    String code
    String name
    String image

    static mapping = {
        id generator: 'uuid'
    }

    static constraints = {
        code nullable: false, blank: false, unique: true
        name nullable: false, blank: false, unique: true
        image nullable: true, blank: true, unique: true
    }

    def toJsonForm = {
        [
            id: id,
            code:code,
            name: name,
            image: image,
        ]
    }

    def toBasicForm = {
        [
                id: id,
                name: name
        ]
    }
}
