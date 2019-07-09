package cn.wpin.bean;

import cn.wpin.bean.resource.ResourceLoader;

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
}
