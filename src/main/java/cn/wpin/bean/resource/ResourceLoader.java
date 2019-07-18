package cn.wpin.bean.resource;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
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

    public ImmutableSet<ClassPath.ClassInfo> getPackageResources(String config){
        ClassPath classpath= null;
        try {
            classpath = ClassPath.from(this.getClass().getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classpath.getTopLevelClassesRecursive(config);
    }

    /**
     * 测试下加载类路径下所有包
     * @param args
     * @throws IOException
     */
    public static void main(String[] args){
        System.out.println(new ResourceLoader().getPackageResources("cn.wpin"));
    }
}
