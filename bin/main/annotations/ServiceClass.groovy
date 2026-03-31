package annotations


import java.lang.annotation.*

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE])
@interface ServiceClass {
    Class clazz()
}