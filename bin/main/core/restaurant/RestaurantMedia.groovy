package core.restaurant

import bases.BaseModel
import media.ImageMedia

class RestaurantMedia  extends BaseModel implements Serializable {
    ImageMedia media

    static belongsTo = [restaurant: Restaurant]

    static mapping = {
        id column: 'id_restaurant_media', generator: 'uuid'
    }

    static constraints = {
        media nullable: false, blank: false
        restaurant nullable: false, blank: false
    }

    def toFirebaseForm = {
        media?.toFirebaseForm()
    }
}
