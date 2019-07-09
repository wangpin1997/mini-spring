package cn.wpin.aop.aspect;

import cn.wpin.bean.factory.BeanFactory;
import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 环绕增强
 * @author wangpin
 */
@Data
public class AspectJAroundAdvice implements MethodInterceptor {

    private BeanFactory beanFactory;

    private Method aspectJAdviceMethod;

    private String aspectInstanceName;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return aspectJAdviceMethod.invoke(beanFactory.getBean(aspectInstanceName),methodInvocation);
    }
}
