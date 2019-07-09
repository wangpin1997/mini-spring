package cn.wpin.bean;

import lombok.Data;

/**
 * @author wangpin
 */
@Data
public class BeanReference {

    private String name;

    private Object value;

    public BeanReference(String name) {
        this.name = name;
    }
}
