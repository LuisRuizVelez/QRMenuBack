package ui

import bases.BaseModel
import com.security.Role

class SectionRole extends BaseModel implements Serializable {
    Role role
    Section section

    static mapping = {
        id generator: 'uuid'
    }

    static constraints = {
        role nullable: false, blank:false
        section nullable: false, blank:false
    }
}
