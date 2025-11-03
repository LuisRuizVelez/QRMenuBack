package core.drink

import bases.BaseModel
import categories.drink.presentation.DrinkPresentation
import catalogs.currency.Currency
import firebase.FBDatabase

class DrinkPrice extends BaseModel implements Serializable {
    Double price
    DrinkPresentation presentation
    Currency currency

    static hasMany = [
            uids: DrinkPriceUid,
    ]

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
    }

    def toJsonForm = {
        [
                id: id,
                price: price,
                presentation: presentation?.toBasicForm(),
                currency: currency?.toBasicForm()
        ]
    }

    def toFirebaseForm = { FBDatabase fbDatabase = null  ->
        [
                price:price,
                presentation: presentation?.getUidProperty(presentation?.uids, fbDatabase),
                currency:currency?.getUidProperty(presentation?.uids, fbDatabase),
        ]
    }


}