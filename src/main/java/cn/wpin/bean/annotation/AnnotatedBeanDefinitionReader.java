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


    public void registerBean(Class<?> annotatedClass) {
        registerBean(annotatedClass, null, (Class<? extends Annotation>[]) null);
    }

    public void registerBean(Class<?> annotatedClass, String name, Class<? extends Annotation>... qualifiers) {
        for (Annotation annotation : annotatedClass.getAnnotations()) {
            if (annotation instanceof Component){
                BeanDefinition definition =new BeanDefinition();
                try {
                    definition.setBean(annotatedClass.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                definition.setBeanClass(annotatedClass);
                String className=annotatedClass.getName().substring(annotatedClass.getName().lastIndexOf(".")+1);
                definition.setBeanClassName(annotatedClass.getName());
                getRegistry().put(className,definition);
            }
        }
    }

    public void loadBeanDefinitions(String config) throws Exception {

    }
}
