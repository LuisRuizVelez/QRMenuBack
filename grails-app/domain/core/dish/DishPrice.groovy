package core.dish

import bases.BaseModel
import categories.dish.presentation.DishPresentation
import catalogs.currency.Currency
import firebase.FBDatabase

class DishPrice extends BaseModel implements Serializable {
    Double price
    DishPresentation presentation
    Currency currency

    static hasMany = [
            uids: DishPriceUid,
    ]

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
