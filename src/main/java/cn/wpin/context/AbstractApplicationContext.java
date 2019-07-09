package cn.wpin.context;

import cn.wpin.bean.BeanPostProcessor;
import cn.wpin.bean.factory.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangpin
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected AbstractBeanFactory beanFactory;

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void refresh() throws Exception {
        loadBeanDefinitions(beanFactory);
        registerBeanPostProcessors(beanFactory);
        onRefresh();
    }

    /**
     * 钩子方法，交给具体的子类实现
     * @param factory
     * @throws Exception
     */
    protected abstract void loadBeanDefinitions(AbstractBeanFactory factory) throws Exception;

    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
        List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
        }
    }

    protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }

}
