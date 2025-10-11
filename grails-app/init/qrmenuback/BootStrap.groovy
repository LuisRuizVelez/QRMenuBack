package qrmenuback

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.security.*
import firebase.FBProject
import firebase.FirebaseConfigService
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired

class BootStrap {
    @Autowired FirebaseConfigService firebaseConfigService

    def init = { servletContext ->
        createTestData()


        for (String url in [
                '/',
                '/index',
                '/index.gsp',
                '/shutdown',
                '/**/favicon.ico',
                '/assets/**',
                '/**/js/**',
                '/**/css/**',
                '/**/images/**',
                '/login',
                '/api/login',
                '/login.*',
                '/login/*']) {
            RequestMap req = RequestMap.findByUrl(url)
            if (req == null)
                new RequestMap(url: url, configAttribute: 'permitAll').save()
        }

        for (String url in [
                '/api/logout',
                '/api/validate',
                '/api/**']) {
            RequestMap req = RequestMap.findByUrl(url)
            if (req == null)
                new RequestMap(url: url, configAttribute: 'IS_AUTHENTICATED_FULLY').save()
        }

        // Initialize Firebase applications
        firebaseConfigService.initializeApplications()
    }


    def destroy = {
        Application.firebaseAppList.each { key, item ->
            item.delete()
        }

        Application.firebaseAppList = new HashMap<String , FirebaseApp>()
    }

    @Transactional
    def createTestData() {
        /*def adminRole = new Role('ROLE_ADMIN').save()
        def userRole = new Role('ROLE_USER').save()

        def testUser = new User('me', 'password').save()
        def adminUser = new User('admin', 'admin').save()

        UserRole.create testUser, userRole
        UserRole.create adminUser, adminRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }

        assert User.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 2*/
    }
}
