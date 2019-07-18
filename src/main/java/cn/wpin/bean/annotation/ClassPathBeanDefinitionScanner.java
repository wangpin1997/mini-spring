package cn.wpin.bean.annotation;

import cn.wpin.bean.AbstractBeanDefinitionReader;
import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.bean.resource.ResourceLoader;
import com.google.common.reflect.ClassPath;

import java.util.Set;

/**
 * 通过类路径扫描解析bean
 *
 * @author wangpin
 */
public class ClassPathBeanDefinitionScanner extends AbstractBeanDefinitionReader {

    private AbstractBeanFactory abstractBeanFactory;

    public ClassPathBeanDefinitionScanner(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }

    public void loadBeanDefinitions(String config) throws Exception {

    }

    public void doScan(String basePackage) {
        Set<ClassPath.ClassInfo> set = getResourceLoader().getPackageResources(basePackage);
        for (ClassPath.ClassInfo o : set) {
            try {
                registerBean(Class.forName(o.getName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
