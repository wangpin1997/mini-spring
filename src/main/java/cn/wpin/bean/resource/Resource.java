package cn.wpin.bean.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 内部资源定位接口
 * @author wangpin
 */
public interface Resource {

    InputStream getInputStream() throws IOException;

    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
}
