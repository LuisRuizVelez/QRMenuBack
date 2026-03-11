package annotations

import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@interface DomainClass {
    Class clazz();
}