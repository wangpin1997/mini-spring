package cn.wpin.bean.annotation;

import cn.wpin.bean.AbstractBeanDefinitionReader;
import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.resource.ResourceLoader;

import java.lang.annotation.Annotation;

/**
 * 通过注解标志解析bean
 * @author wangpin
 */
public class AnnotatedBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public AnnotatedBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }


    public void register(Class<?>... annotatedClasses) {
        for (Class<?> annotatedClass : annotatedClasses) {
            registerBean(annotatedClass);
        }
    }



    @Override
    public void loadBeanDefinitions(String config) throws Exception {

    }
}
