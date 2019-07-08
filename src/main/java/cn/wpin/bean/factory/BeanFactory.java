package cn.wpin.bean.factory;

/**
 * IOC最上层接口
 * @author wangpin
 */
public interface BeanFactory {

    Object getBean(String name) throws Exception;
}
