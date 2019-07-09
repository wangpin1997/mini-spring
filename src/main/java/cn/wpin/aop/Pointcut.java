package cn.wpin.aop;

/**
 * 切入点
 * @author wangpin
 */
public interface Pointcut {

    /**
     * 返回切入点匹配的类过滤器
     * @return
     */
    ClassFilter getClassFilter();

    /**
     * 返回切入点匹配的方法
     * @return
     */
    MethodMatcher getMethodMatcher();
}
