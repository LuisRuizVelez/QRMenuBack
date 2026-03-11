package firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.auth.oauth2.GoogleCredentials
/*import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions*/

import grails.gorm.transactions.Transactional

import qrmenuback.Application

@Transactional
class FirebaseConfigService {

    def initializeApplications() {
        FBProject.list().each { project ->
            try {
                InputStream serviceAccount = this.class.classLoader.getResourceAsStream(project?.configurationFileName)
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount) // Google credentials generation for the Firebase project

                // Firebase App configuration options with credentials and database URL
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setDatabaseUrl(project?.defaultDb)
                        .build()

                // Firestore configuration options with credentials
                /*FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()*/

                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, project?.name) // Firebase app initialization
                //Firestore firestore = firestoreOptions.getService() // Firestore initialization

                Application.firebaseAppList.put(project?.name, firebaseApp)
                //Application.firestoreAppList.put(project?.name, firestore)
            } catch (e){
                log.error("Error initializing Firebase project: ${project.name}", e)
            }
        }

    }
}
