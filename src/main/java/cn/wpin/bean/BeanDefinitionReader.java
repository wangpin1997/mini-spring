package cn.wpin.bean;

import java.io.IOException;

/**
 * 从配置中读取BeanDefinition
 */
public interface BeanDefinitionReader {

    void loadBeanDefinitions(String config) throws Exception;
}
