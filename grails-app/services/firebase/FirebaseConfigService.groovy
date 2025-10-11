package firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import grails.gorm.transactions.Transactional
import qrmenuback.Application

@Transactional
class FirebaseConfigService {

    def initializeApplications() {
        FBProject.list().each { project ->
            try {
                InputStream serviceAccount = this.class.classLoader.getResourceAsStream(project?.configurationFileName)

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(project?.defaultDb)
                        .build()
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, project?.name)
                Application.firebaseAppList.put(project?.name, firebaseApp)

            } catch (e){
                log.error("Error initializing Firebase project: ${project.name}", e)
            }
        }

    }
}
