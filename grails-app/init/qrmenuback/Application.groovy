package qrmenuback

import com.google.cloud.firestore.Firestore
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

import groovy.transform.CompileStatic
import com.google.firebase.FirebaseApp


@CompileStatic
class Application extends GrailsAutoConfiguration {
    public static Map<String , FirebaseApp> firebaseAppList = new HashMap<String , FirebaseApp>()
    public static Map<String , Firestore> firestoreAppList = new HashMap<String , Firestore>()

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}