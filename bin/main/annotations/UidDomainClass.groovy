package annotations

import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@interface UidDomainClass {
    Class clazz()
    String mainAttribute()
}