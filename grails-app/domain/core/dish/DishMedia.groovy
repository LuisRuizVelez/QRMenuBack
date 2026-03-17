package core.dish

import bases.BaseModel
import media.ImageMedia

class DishMedia extends BaseModel implements Serializable {
    ImageMedia media

    static belongsTo = [dish: Dish]

    static mapping = {
        id column: 'id_dish_media', generator: 'uuid'
    }

    static constraints = {
        media nullable: false, blank: false
        dish nullable: false, blank: false
    }

    def toFirebaseForm = {
        media?.toFirebaseForm()
    }
}
