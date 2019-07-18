package cn.wpin.context;

import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.annotation.AnnotatedBeanDefinitionReader;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.factory.AutowireCapableBeanFactory;
import cn.wpin.bean.resource.ResourceLoader;

import java.util.Map;

/**
 * 基于注解的配置
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotatedBeanDefinitionReader reader;



    public AnnotationConfigApplicationContext(AbstractBeanFactory beanFactory) {
        super(beanFactory);
        this.reader=new AnnotatedBeanDefinitionReader(new ResourceLoader());
    }

    public AnnotationConfigApplicationContext(){
        super(new AutowireCapableBeanFactory());
        this.reader=new AnnotatedBeanDefinitionReader(new ResourceLoader());


    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory factory) throws Exception {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : reader.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }

    public void register(Class<?>... annotatedClasses) {
        this.reader.register(annotatedClasses);
    }
}
