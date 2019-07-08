package cn.wpin.bean.factory;

import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.BeanPostProcessor;
import cn.wpin.exception.NoSuchBeanDefinitionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    /**
     * 非常关键，这就是存放所有bean的Map
     */
    private Map<String, BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<String, BeanDefinition>(256);

    private final List<String> beanDefinitionNames = new ArrayList<String>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new NoSuchBeanDefinitionException(beanDefinition.getBeanClassName());
        }
        Object bean=beanDefinition.getBean();
        if (bean!=null){
            bean =doCreateBean(beanDefinition);
            bean=initializeBean(bean,beanName);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
        Object bean=createBeanInstance(beanDefinition);
        beanDefinition.setBean(bean);
        applyPropertyValues(bean,beanDefinition);
        return bean;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        return beanDefinition.getBeanClass().newInstance();
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

    }

    protected Object initializeBean(Object bean,String name){
        //分别进行前置和后置增强
        for (BeanPostProcessor processor : beanPostProcessors) {
            bean = processor.postProcessBeforeInitialization(bean, name);
        }
        //todo:做一些操作

        for (BeanPostProcessor processor : beanPostProcessors) {
            bean = processor.postProcessAfterInitialization(bean, name);
        }
        return bean;
    }



}
