package cn.wpin.bean;

import cn.wpin.bean.annotation.Component;
import cn.wpin.bean.resource.ResourceLoader;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 从xml配置读取
 * @author wangpin
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private Map<String,BeanDefinition> registry;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.registry = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
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

}
