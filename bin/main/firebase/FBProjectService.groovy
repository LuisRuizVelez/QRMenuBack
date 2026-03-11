package firebase

import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class FBProjectService extends BaseService {


    def search(InputData inputData, Map params) {
        FBProject fbProject = inputData.item as FBProject

        List<FBProject> result = FBProject.createCriteria().list(params) {
            if (fbProject?.name != null)
                ilike('name', "%${fbProject?.name}%")

            if (fbProject?.projectName != null)
                ilike('projectName', "%${fbProject?.projectName}%")

        } as  List<FBProject>

        [total: result.totalCount, data: result*.toJsonForm(inputData.fbDatabase)]
    }

    def getOptions() {
        FBProject.list()*.toBasicForm()
    }

    @Transactional
    def save(FBProject _fbProject) {
        _fbProject.save(flush: true, failOnError: true)
    }

    @Transactional
    def update(InputData inputData, FBProject fbProject) {
        fbProject.properties = inputData?.item
        fbProject.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(FBProject fbProject) {
        fbProject.delete(flush: true)
    }
}