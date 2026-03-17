package media

import grails.gorm.transactions.Transactional

@Transactional
class ImageMediaService {

    @Transactional
    def deleteImages(List<String> ids) {
        ImageMedia.createCriteria().list {
            'in'('id', ids)
        }*.delete(flush: true, failOnError: true)

        println "Deleted ${ids.size()} images with ids: ${ids}"
    }
}
