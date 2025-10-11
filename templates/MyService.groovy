import grails.gorm.transactions.Transactional

import utils.InputData
import bases.BaseService
import annotations.LangDomainClass
import annotations.UidDomainClass

/*@UidDomainClass( clazz = $MODEL_NAME$Uid, mainAttribute = "$MODEL$" )
@LangDomainClass( clazz = Lang$MODEL_NAME$, mainAttribute = "$MODEL$" )*/
class $MODEL_NAME$Service extends BaseService {

    def search(InputData inputData, Map params) {
        $MODEL_NAME$ $MODEL$ = inputData?.item as $MODEL_NAME$

        List<$MODEL_NAME$> result = $MODEL_NAME$.createCriteria().list(params) {
            if ($MODEL$.name != null)
                ilike 'name', "%${$MODEL$.name}%"

        } as List<$MODEL_NAME$>

        [total: result.totalCount,  data: result*.toJsonForm()]
    }

    def getOptions() {
        $MODEL_NAME$.list()*.toBasicForm()
    }

    @Transactional
    def save($MODEL_NAME$ _$MODEL$) {
        $MODEL_NAME$ $MODEL$ = _$MODEL$.save(flush: true, failOnError: true)

        if (!$MODEL$)
            throw new Exception("521 : Unable to save object.")

        sendToFirebase $MODEL$
    }

    @Transactional
    def update(InputData inputData, $MODEL_NAME$ $MODEL$) {
        $MODEL$.properties = inputData.item
        $MODEL$.save(flush: true, failOnError: true)

        sendToFirebase $MODEL$
    }

    @Transactional
    def delete($MODEL_NAME$ $MODEL$) {
        deleteFromFirebase $MODEL$

        $MODEL$.delete(flush: true)
    }
}