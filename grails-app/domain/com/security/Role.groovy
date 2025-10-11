package com.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	Role(String authority) {
		this()
		this.authority = authority
	}

	private static final long serialVersionUID = 1

	String authority

	static constraints = {
		authority nullable: false, blank: false, unique: true
	}

	static mapping = {
		cache true
	}

	def toJsonForm =  {
		[
			id: id,
			authority: authority
		]
	}

	def toBasicForm =  {
		[
			id: id,
			name: authority
		]
	}
}
