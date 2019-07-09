package cn.wpin.aop;

import cn.wpin.bean.factory.BeanFactory;

/**
 * @author wangpin
 */
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory);
}
