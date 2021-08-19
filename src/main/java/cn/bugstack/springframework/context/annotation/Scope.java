package cn.bugstack.springframework.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    /**
     * 用于配置作用域的自定义注解，方便通过配置Bean对象注解的时候，拿到Bean对象的作用域。不过一般都使用默认的 singleton
     */
    String value() default "singleton";

}
