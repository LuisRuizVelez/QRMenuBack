package firebase

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class FBChildPathService extends BaseService {

    def search(InputData inputData, Map params) {
        FBChildPath fbChildPath = inputData?.item as FBChildPath

        List<FBChildPath> result = FBChildPath.createCriteria().list(params) {
            if (fbChildPath.componentName != null)
                ilike 'componentName', "%${fbChildPath.componentName}%"

        } as List<FBChildPath>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        FBChildPath.list()*.toBasicForm()
    }

    @Transactional
    def save(FBChildPath _fbChildPath) {
        FBChildPath fbChildPath = _fbChildPath.save(flush: true, failOnError: true)

        if (!fbChildPath)
            throw new Exception("521 : Unable to save object.")

    }

    @Transactional
    def update(InputData inputData, FBChildPath fbChildPath) {
        fbChildPath.properties = inputData.item
        fbChildPath.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(FBChildPath fbChildPath) {
        fbChildPath.delete(flush: true)
    }
}