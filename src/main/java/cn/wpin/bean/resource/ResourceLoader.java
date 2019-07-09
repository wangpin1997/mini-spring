package cn.wpin.bean.resource;

import java.net.URL;

/**
 * 资源加载器，解析xml
 * @author wangpin
 */
public class ResourceLoader {

    public Resource getResource(String config){
        URL url=this.getClass().getClassLoader().getResource(config);
        return new UrlResource(url);
    }
}
