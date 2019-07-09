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

    public TargetSource(Class<?> targetClass,Object target,Class<?>... interfaces) {
        this.targetClass = targetClass;
        this.interfaces = interfaces;
        this.target = target;
    }
}
