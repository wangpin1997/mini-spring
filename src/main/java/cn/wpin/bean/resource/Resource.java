package cn.wpin.bean.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 内部资源定位接口
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
