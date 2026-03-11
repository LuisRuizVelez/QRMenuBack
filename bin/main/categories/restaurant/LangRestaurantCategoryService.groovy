package categories.restaurant

import grails.gorm.transactions.Transactional

import firebase.FBChildPath
import firebase.FBDatabase
import bases.BaseService
import firebase.FirebaseDBService

class LangRestaurantCategoryService extends BaseService {
    FirebaseDBService firebaseDBService

    @Transactional
    def sendToFirebase(String uid, LangRestaurantCategory langRestaurantCategory, FBDatabase fbDataBase){
        FBChildPath path = FBChildPath.findByComponentNameAndFbDatabase(langRestaurantCategory.getClassName(), fbDataBase)

        if ( uid == null || path == null )
            return

        String langCode = langRestaurantCategory?.lang?.twoLetterCode?.toLowerCase()
        def attributes = langRestaurantCategory.toJsonForm(path)
        firebaseDBService.update(path, uid, attributes, langCode)
    }

    @Transactional
    def deleteLangFirebase(String uid, LangRestaurantCategory langRestaurantCategory, FBDatabase fbDataBase){
        FBChildPath path = FBChildPath.findByComponentNameAndFbDatabase(langRestaurantCategory.getClassName(), fbDataBase)

        if (path == null || uid == null)
            return

        String langCode = langRestaurantCategory.lang?.twoLetterCode?.toLowerCase()
        firebaseDBService.remove(path, uid, langCode)
    }
}
