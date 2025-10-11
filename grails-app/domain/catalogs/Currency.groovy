package catalogs

import bases.BaseModel
import catalogs.country.Country

class Currency extends BaseModel implements Serializable {
    String name
    String symbol
    Country country

    static mapping = {
        id column: 'id_currency', generator: 'uuid'
    }

    static constraints = {
        name nullable: false, blank: false, unique: true
        symbol nullable: false, blank: false
        country nullable: true, blank: true
    }

    def toJsonForm = {
        [
            id: id,
            name: name,
            symbol: symbol,
            country: country?.toBasicForm()
        ]
    }
}
