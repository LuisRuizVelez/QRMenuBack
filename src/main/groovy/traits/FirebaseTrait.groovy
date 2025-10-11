package traits

import annotations.LangDomainClass
import annotations.UidDomainClass
import exeptions.MissingRequirementException
import firebase.*
import org.springframework.beans.factory.annotation.Autowired

trait FirebaseTrait {
    @Autowired RealtimeService realtimeService
    @Autowired FirestoreService firestoreService

    Class uidClass = null // Clase del modelo uid
    Class langClass = null // Clase del modelo uid
    def uidObject = null // Objeto del modelo uid
    String mainAttribute = null // Atributo con el que se relaciona le modelo principal con el modelo uid
    FBDatabase fbDatabase = null // Base de datos de firebase donde se guardará el objeto
    FBChildPath path = null // Objeto que contiene el path de firebase del objeto que se enviará
    FBChildPath langPath = null // Objeto que contiene el path de firebase del objeto lang que se enviará
    def attributes = null // atributos del modelo principal que se enviaran a firebase
    String className = null // Nombre de la clase del modelo principal que se enviará a firebase
    boolean  itemHasLang = false // Verifica si el objeto tiene la propiedad langs
    String langPathOriginalUrl = null // Guarda la url original del path de firebase del objeto lang que se enviará



    /**
     * Envía un objeto a Firebase, ya sea a la base de datos en tiempo real o a Firestore,
     * dependiendo de la configuración de la clase asociada.
     *
     * @param item El objeto que se enviará a Firebase. Debe contener información relevante
     *             para determinar su clase y atributos.
     * @param db   La base de datos de Firebase (`FbDataBase`) donde se almacenará el objeto.
     * @throws Exception Si no se encuentra la ruta de Firebase asociada al objeto.
     */
    def sendToFirebase(def item, FBDatabase db = null) {
        setConfiguration item, db // Configurar la clase y el path de firebase del objeto


        // enviar a la base de datos en tiempo real si la configuración de la clase lo permite
        if(fbDatabase?.project?.useRealtime) {
            sendToRealTime()

            if(itemHasLang &&  uidObject?.uid != null && langPath != null)
                item.langs.each { langItem ->
                    sendLangToRealTime langItem
                }
        }


        // enviar a firestore si la configuración de la clase lo permite
        if(fbDatabase?.project?.useFirestore) {
            sendToFirestore()

            if(itemHasLang &&  uidObject?.uid != null && langPath != null)
                item.langs.each { langItem ->
                    sendLangToFirestore langItem
                }
        }
    }


    /**
     * Envía un objeto a la base de datos en tiempo real de Firebase.
     * Si el objeto ya tiene un identificador único (`uid`), actualiza los datos existentes.
     * Si no tiene un identificador único, crea uno nuevo, guarda los datos y actualiza el objeto con el nuevo `uid`.
     *
     * @throws Exception Si ocurre un error al guardar o actualizar los datos en la base de datos en tiempo real.
     * @return void
     */
    def sendToRealTime() {
        if (uidObject.uid != null)
            realtimeService.update path, uidObject?.uid, attributes
        else {
            uidObject.uid = realtimeService.save(path, attributes)
            uidObject.save(flush: true, failOnError: true)
        }

        uidObject.refresh()
    }


    /**
     * Envía un objeto de idioma a la base de datos en tiempo real de Firebase.
     * Actualiza los datos existentes si ya hay una entrada para el idioma.
     *
     * @param itemLang El objeto de idioma que se enviará a la base de datos en tiempo real.
     *                 Debe contener los atributos necesarios para su almacenamiento.
     * @return void
     */
    def sendLangToRealTime(def itemLang) {
        String originalUrl = langPath?.url
        String langCode = itemLang?.lang?.twoLetterCode?.toLowerCase()

        def attributes = itemLang.toFirebaseForm(langPath?.fbDatabase)
        langPath.url = langPath.url.replace("{langCode}", langCode)

        realtimeService.update(langPath, uidObject?.uid, attributes)

        langPath.url = originalUrl // Restaurar la URL original del path
    }



    /**
     * Envía un objeto a Firebase Firestore.
     * Si el objeto ya existe en Firestore (identificado por su `uid`), actualiza los datos existentes.
     * Si el objeto no existe, crea una nueva entrada, guarda los datos y actualiza el objeto con el nuevo `uid`.
     *
     * @throws Exception Si ocurre un error al guardar o actualizar los datos en Firestore.
     * @return void
     */
    def sendToFirestore() {
        def data = firestoreService.get(path, uidObject?.uid)

        if (data != null)
            firestoreService.update(path, uidObject?.uid, attributes)
        else {
            uidObject.uid = firestoreService.save(path, attributes)
            uidObject.save(flush: true, failOnError: true)
        }

        uidObject.refresh()
    }

    /**
     * Envía un objeto de idioma a Firebase Firestore.
     * Si los datos del idioma ya existen en Firestore (identificados por el `uid`), actualiza los datos existentes.
     * Si los datos no existen, crea una nueva entrada y guarda los datos.
     *
     * @param itemLang El objeto de idioma que se enviará a Firestore. Debe contener los atributos necesarios para su almacenamiento.
     * @param uid El identificador único del objeto principal al que pertenece el idioma.
     * @return void
     */
    def sendLangToFirestore(def itemLang) {
        String uid = uidObject?.uid
        String originalUrl = langPath?.url
        String langCode = itemLang?.lang?.twoLetterCode?.toLowerCase()

        def attributes = itemLang.getFirebaseAttributes(langPath?.db)
        langPath.url = langPath.url.replace("{langCode}", langCode)

        def data = firestoreService.get(langPath, uid)

        data != null
                ? firestoreService.update(langPath, uid, attributes)
                : firestoreService.save(langPath, uid, attributes)

        langPath.url = originalUrl // Restaurar la URL original del path
    }

    /**
     * Elimina un objeto de Firebase, ya sea de la base de datos en tiempo real o de Firestore,
     * dependiendo de la configuración de la clase asociada.
     *
     * Si el objeto tiene idiomas asociados, también elimina los datos de idioma correspondientes.
     *
     * @param item El objeto que se eliminará de Firebase. Debe contener información relevante
     *             para determinar su clase y atributos.
     * @param db   La base de datos de Firebase (`FbDataBase`) donde se almacenará el objeto.
     * @throws Exception Si no se encuentra la ruta de Firebase asociada al objeto.
     * @return void
     */
    def deleteFromFirebase(def item, FBDatabase db = null){
        setConfiguration item, db

        // Eliminar de la base de datos de RealTime de Firebase si la configuración de la clase lo permite
        if(fbDatabase?.project?.useRealtime) {

            if(itemHasLang &&  uidObject?.uid != null && langPath != null)
                item.langs.each { langItem ->
                    deleteLangFromRealTime langItem
                }

            realtimeService.remove path, uidObject?.uid
        }


        // eliminar de la base de datos Firestore si la configuración de la clase lo permite
        if(db?.project?.useFirestore) {
            if(itemHasLang &&  uidObject?.uid != null && langPath != null)
                item.langs.each { langItem ->
                    deleteLangFromFirestore langItem
                }

            firestoreService.delete path, uidObject?.uid
        }
    }


    /**
     * Elimina un objeto de idioma de la base de datos en tiempo real de Firebase.
     * Reemplaza el marcador de posición `{langCode}` en la URL del path con el código del idioma
     * y elimina los datos asociados al identificador único (`uid`) del objeto principal.
     *
     * Restaura la URL original del path después de completar la operación.
     *
     * @param itemLang El objeto de idioma que se eliminará de la base de datos en tiempo real.
     *                 Debe contener el código del idioma en formato de dos letras.
     * @return void
     */
    def deleteLangFromRealTime(def itemLang) {
        String langCode = itemLang.lang?.twoLetterCode?.toLowerCase()

        if (langPath == null || uidObject?.uid == null)
            return


        langPath.url = langPath.url.replace("{langCode}", langCode)
        realtimeService.remove(langPath, uidObject?.uid)

        langPath.url = langPathOriginalUrl // Restaurar la URL original del path
    }



    /**
     * Elimina un objeto de idioma de Firebase Firestore.
     * Reemplaza el marcador de posición `{langCode}` en la URL del path con el código del idioma
     * y elimina los datos asociados al identificador único (`uid`) del objeto principal.
     *
     * Restaura la URL original del path después de completar la operación.
     *
     * @param itemLang El objeto de idioma que se eliminará de Firestore.
     *                 Debe contener el código del idioma en formato de dos letras.
     * @return void
     */
    def deleteLangFromFirestore( def itemLang ){
        String langCode = itemLang.lang?.twoLetterCode?.toLowerCase()

        if (langPath == null || uidObject?.uid == null)
            return


        langPath.url = langPath.url.replace("{langCode}", langCode)
        firestoreService.delete langPath, uidObject?.uid

        langPath.url = langPathOriginalUrl // Restaurar la URL original del path
    }




    /**
     * Configura la clase y el path de Firebase del objeto que se enviará.
     * Busca la clase y el path en la base de datos de Firebase según la configuración de la clase del objeto.
     *
     * @param item El objeto que contiene la información de la clase y los atributos a enviar.
     * @param db   La base de datos de Firebase (`FbDataBase`) donde se almacenará el objeto.
     * @throws Exception Si no se encuentra el path de Firebase asociado al objeto.
     */
    def setConfiguration(def item, FBDatabase db = null){
        className = item.getClassName()
        path = (db != null)
                ? FBChildPath.findByComponentNameAndFbDatabase(className, db)
                : FBChildPath.findByComponentName(className)

        if (path == null || path.url.isEmpty())
            throw new MissingRequirementException("Firebase path not found")

        fbDatabase = path?.fbDatabase // obtener la base de datos de firebase del path
        uidClass = this.getClass().getAnnotation(UidDomainClass).clazz()
        mainAttribute = this.getClass().getAnnotation(UidDomainClass).mainAttribute()
        uidObject = uidClass.findOrCreateWhere((mainAttribute): item, fbDatabase: fbDatabase)
        attributes = item.toFirebaseForm(fbDatabase) // obtener los atributos del modelo principal que se enviarán a firebase

        itemHasLang = item?.hasProperty("langs") && item?.langs != null // verificar si el objeto tiene la propiedad langs
        if(itemHasLang){
            langClass = this.getClass().getAnnotation(LangDomainClass).clazz()
            langPath = FBChildPath.findByComponentNameAndFbDatabase(langClass?.simpleName, fbDatabase)
            langPathOriginalUrl = langPath?.url // guardar la url original del path de firebase del objeto lang que se enviará
        }
    }


}