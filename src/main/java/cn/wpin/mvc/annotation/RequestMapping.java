package cn.wpin.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author wangpin
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value() default "";
}
