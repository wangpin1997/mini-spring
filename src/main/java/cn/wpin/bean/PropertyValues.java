package cn.wpin.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装一个对象所有的PropertyValue
 * @author wangpin
 */
@Data
@NoArgsConstructor
public class PropertyValues {

    private final List<PropertyValue> propertyValueList=new ArrayList();


    public void addPropertyValue(PropertyValue value){
        this.propertyValueList.add(value);
    }


}
