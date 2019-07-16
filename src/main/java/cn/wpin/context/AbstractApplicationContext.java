package cn.wpin.context;

import cn.wpin.bean.BeanPostProcessor;
import cn.wpin.bean.factory.AbstractBeanFactory;

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
        //先加载默认自动装配的bean
        loadBeanDefinitions(beanFactory);
        //再注册bean
        registerBeanPostProcessors(beanFactory);
        //准备工作，记录下容器的启动时间、标记“已启动”状态、处理配置文件中的占位符
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
