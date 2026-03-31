package media

import bases.BaseModel

class ImageMedia extends BaseModel implements Serializable {
    String name
    String url
    String path
    Boolean isThumb

    static mapping = {
        id column: 'id_image_media', generator: 'uuid'
    }

    static constraints = {
        name    nullable: false, blank: false
        url     nullable: false, blank: false
        path    nullable: false, blank: false
        isThumb nullable: false, blank: false
    }

    def toFirebaseForm = {
        [
                name    : name,
                url     : url,
                path    : path,
                isThumb : isThumb,

        ]
    }
}
