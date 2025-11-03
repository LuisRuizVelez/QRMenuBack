package core.dish

import bases.BaseModel
import categories.dish.presentation.DishPresentation
import catalogs.currency.Currency

class DishPrice extends BaseModel implements Serializable {
    Double price
    DishPresentation presentation
    Currency currency

    static belongsTo = [
            dish: Dish,
    ]

    static mapping = {
        id column:'id_dish_price', generator: 'uuid'
    }

    static constraints = {
        price nullable: false, blank: false
        presentation nullable: true, blank: true
        currency nullable: false, blank: false
        dish nullable: false, blank: false
        unique(['dish', 'presentation', 'currency'])
    }
}
