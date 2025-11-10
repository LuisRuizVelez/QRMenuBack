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
	Boolean isToGrouping

	static hasMany = [childRoles: Role]
	static belongsTo = [parentRole: Role]


	static constraints = {
		authority nullable: false, blank: false, unique: true
		isToGrouping nullable: false, blank: false
		parentRole nullable: true, blank: false
	}

	static mapping = {
		cache true
	}

	def toJsonForm =  {
		[
			id: id,
			authority: authority,
			isToGrouping: isToGrouping,
			parentRole: !parentRole ? null : [
					id: parentRole?.id,
					name: parentRole?.authority
			],
		]
	}

	def toBasicForm =  {
		[
			id: id,
			name: authority
		]
	}
}
