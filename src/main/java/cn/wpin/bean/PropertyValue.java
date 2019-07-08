package cn.wpin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangpin
 */
@Data
@AllArgsConstructor
public class PropertyValue {

    private String name;

    private Object value;
}
