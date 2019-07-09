package cn.wpin.aop.aspect;

import cn.wpin.aop.BeanFactoryAware;
import cn.wpin.aop.ProxyFactory;
import cn.wpin.aop.TargetSource;
import cn.wpin.bean.BeanPostProcessor;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.factory.BeanFactory;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

public class AspectJAwareAdvisorAutoProxyCreator implements BeanFactoryAware, BeanPostProcessor {

    private AbstractBeanFactory beanFactory;


    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory= (AbstractBeanFactory) beanFactory;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if (bean instanceof AspectJExpressionPointcutAdvisor) {
            return bean;
        }
        if (bean instanceof MethodInterceptor) {
            return bean;
        }
        List<AspectJExpressionPointcutAdvisor> advisors = beanFactory
                .getBeansForType(AspectJExpressionPointcutAdvisor.class);
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
                ProxyFactory advisedSupport = new ProxyFactory();
                advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                TargetSource targetSource = new TargetSource( bean.getClass(),bean,bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);

                return advisedSupport.getProxy();
            }
        }
        return bean;
    }
}
