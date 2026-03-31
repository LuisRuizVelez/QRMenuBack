package annotations

import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@interface MediaDomainClass {
    Class clazz()
    String mainAttribute()
}