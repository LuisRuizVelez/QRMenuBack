package firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import grails.gorm.transactions.Transactional

import qrmenuback.Application

@Transactional
class FirebaseDBService {

    /**
     * Guarda un valor en Firebase bajo la ruta especificada por FBChildPath.
     *
     * @param fBChildPath La ruta del nodo en Firebase.
     * @param value El valor a guardar en el nodo.
     * @return La clave generada para el nuevo nodo.
     */
    String save(FBChildPath fBChildPath, def value, String langCode = null) {
        if(!fBChildPath || !value)
            return null

        String projectName = fBChildPath?.getProjectName()
        String urlDB = fBChildPath?.getDBUrl()
        String referencePath = fBChildPath?.getPath( langCode )

        return saveInFirebase(projectName, urlDB, referencePath, value)
    }


    /**
     * Envía un valor a Firebase bajo la ruta especificada.
     *
     * @param projectName El nombre del proyecto de Firebase.
     * @param urlDB La URL de la base de datos de Firebase.
     * @param referencePath La ruta de referencia en la base de datos.
     * @param value El valor a enviar.
     * @return La clave generada para el nuevo nodo.
     */
    String saveInFirebase(String projectName, String urlDB, String referencePath, def value) {
        FirebaseApp firebaseApp = Application.firebaseAppList.get( projectName )
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, urlDB)
        DatabaseReference reference = database.getReference(referencePath)

        String key = reference.push().getKey()

        if(!key)
            return null

        reference.child(key).setValueAsync(value)


        return key
    }


    /**
     * Actualiza un valor en Firebase bajo la ruta especificada por FBChildPath.
     *
     * @param fBChildPath La ruta del nodo en Firebase.
     * @param value El nuevo valor a actualizar en el nodo.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    def update(FBChildPath fBChildPath, String key, Map<String, Object> value, String langCode = null){
        if(!fBChildPath || !key || !value)
            return null

        String projectName = fBChildPath?.getProjectName()
        String urlDB = fBChildPath?.getDBUrl()
        String referencePath = fBChildPath?.getPath( langCode )

        return updateInFirebase(projectName, urlDB, referencePath, key, value)
    }


    /**
     * Actualiza los valores de un nodo específico en Firebase bajo la ruta especificada.
     *
     * @param projectName El nombre del proyecto de Firebase.
     * @param urlDB La URL de la base de datos de Firebase.
     * @param referencePath La ruta de referencia en la base de datos.
     * @param key La clave del nodo que se desea actualizar.
     * @param value Un mapa con los valores que se desean actualizar en el nodo.
     * @return Una tarea asincrónica que representa el estado de la operación de actualización.
     */
    def updateInFirebase(String projectName, String urlDB, String referencePath, String key, Map value){
        FirebaseApp firebaseApp = Application.firebaseAppList.get(projectName)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, urlDB)

        DatabaseReference reference = database.getReference(referencePath).child(key)
        return reference.updateChildrenAsync(value)
    }


    /**
     * Elimina un nodo en Firebase bajo la ruta especificada por FBChildPath.
     *
     * @param fBChildPath La ruta del nodo en Firebase.
     * @param key La clave del nodo que se desea eliminar.
     * @param langCode El código de idioma opcional para la ruta.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    def remove(FBChildPath fBChildPath, String key, String langCode = null){
        if(!fBChildPath || !key)
            return null

        String projectName = fBChildPath?.getProjectName()
        String urlDB = fBChildPath?.getDBUrl()
        String referencePath = fBChildPath?.getPath( langCode )

        return removeFromFirebase(projectName, urlDB, referencePath, key)
    }


    /**
     * Elimina un nodo específico en Firebase bajo la ruta especificada.
     *
     * @param projectName El nombre del proyecto de Firebase.
     * @param urlDB La URL de la base de datos de Firebase.
     * @param referencePath La ruta de referencia en la base de datos.
     * @param key La clave del nodo que se desea eliminar.
     * @return Una tarea asincrónica que representa el estado de la operación de eliminación.
     */
    def removeFromFirebase(String projectName, String urlDB, String referencePath, String key){
        FirebaseApp firebaseApp = Application.firebaseAppList.get(projectName)
        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseApp, urlDB)

        return database.getReference(referencePath).child(key).removeValue()
    }
}
