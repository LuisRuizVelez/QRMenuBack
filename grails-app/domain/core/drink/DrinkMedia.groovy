package core.drink

import bases.BaseModel
import media.ImageMedia

class DrinkMedia extends BaseModel implements Serializable {
    ImageMedia media

    static belongsTo = [drink: Drink]

    static mapping = {
        id column: 'id_drink_media', generator: 'uuid'
    }

    static constraints = {
        media nullable: false, blank: false
        drink nullable: false, blank: false
    }

    def toFirebaseForm = {
        media?.toFirebaseForm()
    }
}
