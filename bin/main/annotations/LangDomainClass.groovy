package annotations

import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@interface LangDomainClass {
    Class clazz()
    String mainAttribute()
}