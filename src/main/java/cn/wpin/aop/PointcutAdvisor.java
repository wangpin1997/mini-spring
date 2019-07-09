package cn.wpin.aop;

public interface PointcutAdvisor extends Advisor {

    /**
     * 获取驱动此顾问程序的切入点
     */
    Pointcut getPointcut();
}
