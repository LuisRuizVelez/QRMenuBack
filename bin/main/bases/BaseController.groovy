package bases

import grails.web.Action
import grails.util.Holders
import firebase.StorageService
import org.springframework.http.HttpStatus
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartHttpServletRequest

import media.ImageMedia
import annotations.DomainClass
import annotations.MediaDomainClass
import annotations.ServiceClass


class BaseController {
    @Autowired StorageService storageService


    /**
     * Handles the addition of an image to a domain object.
     *
     * This method performs the following steps:
     * 1. Retrieves the domain class, media domain class, and service class using annotations.
     * 2. Fetches the domain object based on the provided `id` parameter.
     * 3. Validates the existence of the domain object. If not found, returns a 404 status.
     * 4. Processes multipart file uploads from the request.
     * 5. Uploads the file using the `StorageService` and creates an `ImageMedia` object.
     * 6. Associates the uploaded image with the domain object and saves it.
     * 7. Sends the updated domain object to Firebase using the service class.
     *
     * Exceptions:
     * - Catches `ValidationException` and responds with a 422 status.
     * - Catches generic exceptions and responds with a 422 status.
     *
     * HTTP Responses:
     * - 200 OK: If the image is successfully added.
     * - 404 NOT FOUND: If the domain object is not found.
     * - 422 UNPROCESSABLE ENTITY: If validation errors occur.
     * - 500 INTERNAL SERVER ERROR: If other errors occur.
     */
    @Action
    @Transactional
    def addImage(){
        Class domainClass = this.getClass().getAnnotation(DomainClass)?.clazz()
        Class serviceClass = this.getClass().getAnnotation(ServiceClass)?.clazz()
        Class mediaDomainClass = this.getClass().getAnnotation(MediaDomainClass)?.clazz()
        String mainAttribute = this.getClass().getAnnotation(MediaDomainClass)?.mainAttribute()

        boolean clearImage = false
        def mediaObject = null
        def service = Holders.grailsApplication.mainContext.getBean(serviceClass)

        try {
            def domainObject = domainClass.get(params?.id as String)
            Boolean isThumb = params?.isThumb == "true"

            if (domainObject == null) {
                render status: HttpStatus.NOT_FOUND
                return
            }

            MultipartHttpServletRequest mpr = request as MultipartHttpServletRequest

            // Itera sobre los archivos multipart y procesa cada uno
            mpr?.multipartFiles?.each { key, item ->
                MultipartFile multipartFile = mpr.getFile(key)

                Map imageProperties = storageService.uploadFile domainObject, multipartFile, isThumb

                if(imageProperties != null){
                    ImageMedia imageMedia = new ImageMedia(imageProperties)
                    imageMedia.save(flush: true, failOnError: true)

                    mediaObject = mediaDomainClass.newInstance((mainAttribute): domainObject, media: imageMedia)
                    mediaObject.save(flush: true, failOnError: true)
                }
            }

            service.sendToFirebase domainObject

            render status: HttpStatus.OK
        } catch (ValidationException ex) {
            ex.printStackTrace()
            clearImage = true

            respond ex, [status: HttpStatus.UNPROCESSABLE_ENTITY]
        } catch (Exception ex) {
            ex.printStackTrace()
            clearImage = true

            respond ex, [status: HttpStatus.INTERNAL_SERVER_ERROR]
        } finally {
            println "Ejecutando bloque finally. clearImage: ${clearImage}, mediaObject: ${mediaObject}"
            if(clearImage)
                storageService.deleteFile mediaObject?."$mainAttribute", mediaObject?.media?.name
        }
    }
}
