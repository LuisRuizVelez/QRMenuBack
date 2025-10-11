package firebase

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import qrmenuback.Application

/**
 * Servicio para interactuar con Firestore, proporcionando métodos para guardar, actualizar, eliminar y obtener documentos.
 */
class FirestoreService {

    /**
     * Guarda un nuevo documento en Firestore dentro de la colección especificada.
     *
     * @param path El objeto FBChildPath que contiene la información de la base de datos y la URL de la colección en Firestore.
     * @param data Un mapa que representa los datos que se guardarán en el nuevo documento.
     * @return El ID del documento recién creado en Firestore.
     */
    def save(FBChildPath path,  Map<String, Object> data) {
        Firestore firestore = Application.firestoreAppList.get(path?.fbDatabase?.project?.name)
        DocumentReference reference = firestore.collection(path?.urlToFirestore()).add(data).get()

        return reference.getId()
    }


    /**
     * Guarda un documento en Firestore dentro de la colección especificada, utilizando un ID de documento proporcionado.
     *
     * @param path El objeto FBChildPath que contiene la información de la base de datos y la URL de la colección en Firestore.
     * @param documentId El ID del documento que se utilizará para guardar los datos.
     * @param data Un mapa que representa los datos que se guardarán en el documento.
     * @return El ID del documento recién creado o actualizado en Firestore.
     */
    def save(FBChildPath path, String documentId,  Map<String, Object> data) {
        Firestore firestore = Application.firestoreAppList.get(path?.fbDatabase?.project?.name)
        DocumentReference reference = firestore.collection(path?.urlToFirestore()).document(documentId)

        reference.set(data).get()

        return reference.getId()
    }


    /**
     * Actualiza un documento existente en Firestore dentro de la colección especificada.
     *
     * @param path El objeto FBChildPath que contiene la información de la base de datos y la URL de la colección en Firestore.
     * @param documentId El ID del documento que se actualizará.
     * @param data Un mapa que representa los datos que se actualizarán en el documento.
     */
    def update(FBChildPath path, String documentId, Map<String, Object> data) {
        Firestore firestore = Application.firestoreAppList.get(path?.fbDatabase?.project?.name)
        firestore.collection(path?.urlToFirestore()).document(documentId).update(data).get()
    }


    /**
     * Elimina un documento existente en Firestore dentro de la colección especificada.
     *
     * @param path El objeto FBChildPath que contiene la información de la base de datos y la URL de la colección en Firestore.
     * @param documentId El ID del documento que se eliminará.
     */
    def delete(FBChildPath path, String documentId) {
        Firestore firestore = Application.firestoreAppList.get(path?.fbDatabase?.project?.name)
        firestore.collection(path?.urlToFirestore()).document(documentId).delete().get()
    }


    /**
     * Obtiene los datos de un documento existente en Firestore dentro de la colección especificada.
     *
     * @param path El objeto FBChildPath que contiene la información de la base de datos y la URL de la colección en Firestore.
     * @param documentId El ID del documento que se desea obtener.
     * @return Un mapa que representa los datos del documento, o null si el ID del documento no es válido.
     */
    def get(FBChildPath path, String documentId) {
        if(!documentId)
            return null

        Firestore firestore = Application.firestoreAppList.get(path?.fbDatabase?.project?.name)
        String urlToFirestore = path?.urlToFirestore()
        return firestore.collection(urlToFirestore).document(documentId).get().get().getData()
    }
}
