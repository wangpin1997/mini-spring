package cn.wpin.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author wangpin
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    String value() default "";
}
