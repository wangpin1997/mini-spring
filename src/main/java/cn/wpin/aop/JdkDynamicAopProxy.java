package cn.wpin.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理
 * @author wangpin
 */
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        super(advisedSupport);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInterceptor methodInterceptor=advisedSupport.getMethodInterceptor();
        if (advisedSupport.getMethodMatcher()!=null&&
                advisedSupport.getMethodMatcher().matches(method,advisedSupport.getTargetSource().getTarget().getClass())){
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(),method,args));
        }
        return method.invoke(advisedSupport.getTargetSource().getTarget(),args);
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupport.getTargetSource().getInterfaces(),this);
    }
}
