package cn.wpin.aop;

/**
 * 过滤器限制切入点或介绍与给定目标类集的匹配
 * @author wangpin
 */
public interface ClassFilter {

    /**
     * 切入点是否应用于给定的接口或目标类
     * @param targetClass
     * @return
     */
    boolean matches(Class<?> targetClass);
}
