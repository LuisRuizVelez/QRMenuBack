package com.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    User(String username, String password) {
        this()
        this.username = username
        this.password = password
    }

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
	    password column: '`password`'
    }


    def toJsonForm() {
        [
            id: id,
            username: username,
            password: password,
            enabled: enabled,
            accountExpired: accountExpired,
            accountLocked: accountLocked,
            passwordExpired: passwordExpired,
            roles: getAuthorities()
        ]
    }

    def toBasicForm() {
        [
            id: id,
            username: username
        ]
    }
}
