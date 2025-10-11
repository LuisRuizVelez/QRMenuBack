grails.plugin.springsecurity.useSecurityEventListener = true

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.security.UserRole'
grails.plugin.springsecurity.authority.className = 'com.security.Role'

grails.plugin.springsecurity.requestMap.className = 'com.security.RequestMap'
grails.plugin.springsecurity.securityConfigType = "Requestmap"
grails.plugin.springsecurity.filterChain.chainMap = [
		[pattern: '/api/**', filters:'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter'],
		[pattern: '/**', filters:'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']
]

grails.plugin.springsecurity.rest.logout.endpointUrl = '/api/logout'
grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'com.security.AuthenticationToken'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = "tokenValue"
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = "username"











//grails.plugin.springsecurity.useSecurityEventListener = true
//
//// Added by the Spring Security Core plugin:
//grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.security.User'
//grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.security.UserRole'
//grails.plugin.springsecurity.authority.className = 'com.security.Role'
//grails.plugin.springsecurity.requestMap.className = 'com.security.RequestMap'
//grails.plugin.springsecurity.securityConfigType = "Requestmap"
//
//
//
//
//
//
//grails.plugin.springsecurity.rest.token.storage.useGorm = true
//grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'com.security.AuthenticationToken'
//grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = "tokenValue"
//grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = "username"
//
//grails.plugin.springsecurity.controllerAnnotations.staticRules = [
//		[pattern: '/',               access: ['permitAll']],
//		[pattern: '/error',          access: ['permitAll']],
//		[pattern: '/index',          access: ['permitAll']],
//		[pattern: '/index.gsp',      access: ['permitAll']],
//		[pattern: '/shutdown',       access: ['permitAll']],
//		[pattern: '/assets/**',      access: ['permitAll']],
//		[pattern: '/**/js/**',       access: ['permitAll']],
//		[pattern: '/**/css/**',      access: ['permitAll']],
//		[pattern: '/**/images/**',   access: ['permitAll']],
//		[pattern: '/**/favicon.ico', access: ['permitAll']],
//		[pattern: '/api/logout', access: ['isAuthenticated()']],
//		[pattern: '/api/**', access: ['IS_AUTHENTICATED_FULLY']]
//]
//
//grails.plugin.springsecurity.filterChain.chainMap = [
//		[pattern: '/api/**', filters:'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter'],
//		[pattern: '/**', filters:'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter']
//]