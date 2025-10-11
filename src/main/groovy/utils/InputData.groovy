package utils

import firebase.FBDatabase
import grails.validation.Validateable

class InputData implements Serializable, Validateable {
    FBDatabase fbDatabase
    Map<String, Object> item = [:]
    List<Object> items = []
}
