package cn.wpin.context;

import cn.wpin.bean.annotation.AnnotatedBeanDefinitionReader;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.resource.ResourceLoader;

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
        this.reader=new AnnotatedBeanDefinitionReader(new ResourceLoader());

    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory factory){

    }

    public void register(Class<?>... annotatedClasses) {
        this.reader.register(annotatedClasses);
    }
}
