package cn.wpin.bean.factory;

import cn.wpin.aop.BeanFactoryAware;
import cn.wpin.bean.BeanDefinition;
import cn.wpin.bean.BeanReference;
import cn.wpin.bean.PropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 可自动装配内容的bean
 * @author wangpin
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    public void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }
            try {
                Method declaredMethod = bean.getClass().getDeclaredMethod(
                        "set" + propertyValue.getName().substring(0, 1).toUpperCase()
                                + propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);

                declaredMethod.invoke(bean, value);
            } catch (NoSuchMethodException e) {
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }
}
