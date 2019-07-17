package cn.wpin.bean.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
