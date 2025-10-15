package core.drink

import bases.BaseModel
import categories.drink.presentation.DrinkPresentation

class DrinkPrice extends BaseModel implements Serializable {
    Double price
    DrinkPresentation presentation
    Currency currency

    static belongsTo = [
            drink: Drink,
    ]

    static mapping = {
        id column:'id_dish_price', generator: 'uuid'
    }

    static constraints = {
        price nullable: false, blank: false
        presentation nullable: true, blank: true
        currency nullable: false, blank: false
        drink nullable: false, blank: false
        unique(['dish', 'presentation', 'currency'])
    }
}