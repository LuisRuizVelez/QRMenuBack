package bases

import firebase.FBDatabase
import traits.StringValidationsTrait

class BaseModel implements StringValidationsTrait {
    String id

    def getDefaultLangProperty = { def langs, String property ->
        def defaultLang = langs.find{ it?.lang?.isDefault }
        return defaultLang?."$property"
    }

    def getUidProperty = { def uids, FBDatabase fbDatabase ->
        def uid = uids.find{ it?.fbDatabase?.id == fbDatabase?.id }
        return uid?.uid
    }

    def getClassName = {
        return this.class.simpleName
    }
}
