package categories.menu

import bases.BaseModel
import core.menu.Menu
import media.ImageMedia


class MenuMedia extends BaseModel implements Serializable {
    ImageMedia media

    static belongsTo = [menu: Menu]

    static mapping = {
        id column: 'id_menu_media', generator: 'uuid'
    }

    static constraints = {
        media nullable: false, blank: false
        menu nullable: false, blank: false
    }



    def toFirebaseForm = {
        media?.toFirebaseForm()
    }



    def beforeDelete() {
        media?.delete()
    }
}
