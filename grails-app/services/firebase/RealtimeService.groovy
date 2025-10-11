package firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import grails.gorm.transactions.Transactional
import qrmenuback.Application

@Transactional
class RealtimeService {

    String save(FBChildPath path, Map<String, String> value) {
        return sendFirebase(path, value)
    }



    /**
     * Actualiza un nodo en Firebase con los valores proporcionados.
     *
     * @param path La ruta del nodo en Firebase.
     * @param value Los valores a actualizar en el nodo.
     */
    def update(FBChildPath path, Map value){
        FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
        DatabaseReference reference = database.getReference(path.url)
        reference.setValueAsync(value)
    }

    def update(FBChildPath path, String key, Map<String, Object> value){
        FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
        DatabaseReference reference = database.getReference(path.url).child(key)
        reference.updateChildrenAsync(value)
    }

    def update(FBChildPath path, String key, Object value) {
        FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
        DatabaseReference reference = database.getReference(path.url).child(key)
        reference.updateChildrenAsync(value as Map<String, Object>)
    }

    def update(FBChildPath path, String key, List value) {
        FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
        DatabaseReference reference = database.getReference(path.url).child(key)
        reference.setValueAsync(value)
    }

    def remove(FBChildPath path, String key){
        FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
        DatabaseReference reference = database.getReference(path.url).child(key).removeValue()
    }

    String sendFirebase(FBChildPath path, def value){
        if (path){
            FirebaseApp firebaseApp = Application.firebaseAppList.get(path.fbDatabase?.project?.name)
            FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, path.fbDatabase?.url)
            DatabaseReference reference = database.getReference(path.url)
            String key = reference.push().getKey()
            if (key != null) {
                reference.child(key).setValueAsync(value)
            }
            return key
        }
        return null
    }



    /**
     * Obtiene el path de firebase de la clase proporcionada.
     * @param clazz Clase de la que se esta buscando el path.
     * @param fbDataBase base de datos de firebase en la que se busca.
     * @param args a reemplazar en el path
     * @return
     */
    String getURLFirebase(Class<?> clazz, FBDatabase fbDataBase, Map args = [:]){
        FBChildPath path = FBChildPath.findByComponentNameAndFbDatabase(clazz.simpleName, fbDataBase)
        String url = path?.url

        if(args?.uid)
            url = url.replace('{uid}', args?.uid)

        if(args?.langCode)
            url = url.replace('{langCode}', args?.langCode)

        if(args?.complementUri)
            url += args?.complementUri


        return url
    }
}
