package ui

import com.security.Role
import com.security.User
import com.security.UserRole
import grails.gorm.transactions.Transactional

import bases.BaseService
import utils.InputData

class SectionService extends BaseService {

    def search(InputData inputData, Map params) {
        Section section = inputData?.item as Section

        List<Section> result = Section.createCriteria().list(params) {
            if (section.name != null)
                ilike 'name', "%${section.name}%"

        } as List<Section>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        Section.list()*.toBasicForm()
    }

    @Transactional
    def save(Section _section) {
        Section section = _section.save(flush: true, failOnError: true)

        if (!section)
            throw new Exception("521 : Unable to save object.")

    }

    @Transactional
    def update(InputData inputData, Section section) {
        section.properties = inputData.item
        section.save(flush: true, failOnError: true)
    }

    @Transactional
    def delete(Section section) {
        section.delete(flush: true)
    }


    def getSectionsByUser(User user){
        Set<Role> roles = UserRole.findAllByUser(user)?.role as Set

        List<Section> sections = SectionRole.createCriteria().list() {
            inList 'role', roles

            projections {
                property 'section'
            }
        } as List<Section>

        return sections*.toJsonForm()

    }
}
