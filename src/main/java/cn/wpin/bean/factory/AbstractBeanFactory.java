package cn.wpin.bean.factory;

import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.BeanPostProcessor;
import cn.wpin.exception.NoSuchBeanDefinitionException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangpin
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    /**
     * 非常关键，这就是存放所有bean的Map
     */
    private Map<String, BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<String, BeanDefinition>(256);

    private final Set<String> beanDefinitionNames = new HashSet<String>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new NoSuchBeanDefinitionException(beanDefinition.getBeanClassName());
        }
        Object bean=beanDefinition.getBean();
        if (bean==null){
            bean =doCreateBean(beanDefinition);
            bean=initializeBean(bean,beanName);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    public int getBeanCount(){
        return this.beanDefinitionMap.size();
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

    protected Object initializeBean(Object bean,String name) throws Exception {
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


    public  List getBeansForType(Class<?> type) throws Exception {
        List beans = new ArrayList<Object>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }
        return beans;
    }

    public  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public void preInstantiateSingletons() throws Exception {
        for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
            String beanName = (String) it.next();
            getBean(beanName);
        }
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }
}
