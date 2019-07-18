package cn.wpin.context;

import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.annotation.AnnotatedBeanDefinitionReader;
import cn.wpin.bean.annotation.ClassPathBeanDefinitionScanner;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.factory.AutowireCapableBeanFactory;
import cn.wpin.bean.resource.Resource;
import cn.wpin.bean.resource.ResourceLoader;

import java.util.Map;

/**
 * 基于注解的配置
 * @author wangpin
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotatedBeanDefinitionReader reader;

    private final ClassPathBeanDefinitionScanner scanner;



    public AnnotationConfigApplicationContext(AbstractBeanFactory beanFactory) {
       this(beanFactory,new ResourceLoader());
    }

    public AnnotationConfigApplicationContext(AbstractBeanFactory beanFactory, ResourceLoader resource){
        super(beanFactory);
        this.reader=new AnnotatedBeanDefinitionReader(resource);
        this.scanner=new ClassPathBeanDefinitionScanner(resource);
    }

    public AnnotationConfigApplicationContext(){
       this(new AutowireCapableBeanFactory());
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory factory) throws Exception {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : reader.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : scanner.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }

    /**
     * 注册单个或者多个bean
     * @param annotatedClasses
     */
    public void register(Class<?>... annotatedClasses) {
        this.reader.register(annotatedClasses);
    }


    /**
     * 根据路径扫描
     * @param basePackages
     */
    public void scan(String ...basePackages){
        scanner.scan(basePackages);
    }
}
