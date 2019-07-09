package cn.wpin.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 一个时间拦截器，计算方法运行多久
 */
public class TimeInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime=System.nanoTime();
        System.out.println("开始时间："+startTime);
        Object proceed=invocation.proceed();
        long endTime=System.nanoTime();
        System.out.println("结束时间："+endTime+"\n"+"时间差为"+(endTime-startTime));
        return proceed;
    }

}
