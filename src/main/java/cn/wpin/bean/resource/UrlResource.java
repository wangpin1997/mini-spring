package cn.wpin.bean.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 资源处理，主要是解析xml
 * @author wangpin
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    public InputStream getInputStream() throws IOException {
        URLConnection connection=url.openConnection();
        connection.connect();
        return connection.getInputStream();
    }
}
