package cn.wpin.bean;

/**
 * 此类是aop很关键的一个雷，就两个方法，前置和后置
 * @author wangpin
 */
public interface BeanPostProcessor {

    /**
     * 初始化之前执行
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean,String beanName);

    /**
     * 初始化之后执行
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean,String beanName) throws Exception;
}
