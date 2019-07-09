package cn.wpin.context;

import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.factory.AutowireCapableBeanFactory;
import cn.wpin.bean.resource.ResourceLoader;
import cn.wpin.bean.xml.XmlBeanDefinitionReader;

import java.util.Map;

/**
 * 基于xml配置具体入口实现层
 * @author wangpin
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private String configLocation;

    public ClassPathXmlApplicationContext(String configLocation) throws Exception {
        this(configLocation,new AutowireCapableBeanFactory());
    }
    public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
        super(beanFactory);
        this.configLocation = configLocation;
        refresh();
    }


    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory factory) throws Exception {
        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(new ResourceLoader());
        reader.loadBeanDefinitions(configLocation);
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : reader.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }
}
