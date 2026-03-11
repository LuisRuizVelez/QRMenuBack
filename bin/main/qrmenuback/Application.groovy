package qrmenuback

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import groovy.transform.CompileStatic

import com.google.firebase.FirebaseApp
import com.google.cloud.firestore.Firestore



@CompileStatic
class Application extends GrailsAutoConfiguration {
    public static Map<String , FirebaseApp> firebaseAppList = new HashMap<String , FirebaseApp>()
    public static Map<String , Firestore> firestoreAppList = new HashMap<String , Firestore>()

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}