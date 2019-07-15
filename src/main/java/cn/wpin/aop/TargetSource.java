package cn.wpin.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 被代理对象
 * @author wangpin
 */
@Data
public class TargetSource {

    private Class<?> targetClass;

    private Class<?>[] interfaces;

    private Object target;

    /**
     * @param targetClass 目标类的反射
     * @param target 目标对象
     * @param interfaces 目标类实现的多个接口
     */
    public TargetSource(Class<?> targetClass,Object target,Class<?>... interfaces) {
        this.targetClass = targetClass;
        this.interfaces = interfaces;
        this.target = target;
    }
}
