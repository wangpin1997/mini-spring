package cn.wpin.aop;

import java.lang.reflect.Method;

/**
 * 检查目标方法是否符合advice的条件
 * @author wangpin
 */
public interface MethodMatcher {


    /**
     * 检查给定的方法是否匹配
     * @param method
     * @param targetClass
     * @return
     */
    boolean matches(Method method, Class<?> targetClass);

}
