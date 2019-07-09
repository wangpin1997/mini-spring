package cn.wpin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 所有的bean都会被包装成该类型的bean
 * @author wangpin
 */
@Data
@NoArgsConstructor
public class BeanDefinition {

    private Object bean;

    private Class<?> beanClass;

    private String beanClassName;

    private PropertyValues propertyValues;

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
        try {
            Class<?> clazz=Class.forName(this.beanClassName);
            this.beanClass=clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
