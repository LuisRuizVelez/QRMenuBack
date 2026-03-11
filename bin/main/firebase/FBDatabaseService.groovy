package firebase


import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class FBDatabaseService extends BaseService {

    def search(InputData inputData, Map params) {
        FBDatabase fbDatabase = inputData?.item as FBDatabase

        List<FBDatabase> result = FBDatabase.createCriteria().list(params) {
            if (fbDatabase.name != null)
                ilike 'name', "%${fbDatabase.name}%"

            if (fbDatabase.isDefault != null)
                eq 'isDefault', fbDatabase.isDefault


            if (fbDatabase.isProduction != null)
                eq 'isProduction', fbDatabase.isProduction

        } as List<FBDatabase>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        FBDatabase.list()*.toBasicForm()
    }

    @Transactional
    def save(FBDatabase _fbDatabase) {
        FBDatabase fbDatabase = _fbDatabase.save(flush: true, failOnError: true)

        if (!fbDatabase)
            throw new Exception("521 : Unable to save object.")

    }

    @Transactional
    def update(InputData inputData, FBDatabase fbDatabase) {
        fbDatabase.properties = inputData.item
        fbDatabase.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(FBDatabase fbDatabase) {
        fbDatabase.delete(flush: true)
    }
}
