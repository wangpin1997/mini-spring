package cn.wpin.context;

import cn.wpin.business.HelloService;
import org.junit.Test;

public class ApplicationContextTest {


    @Test
    public void test() throws Exception {
        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        HelloService service= (HelloService) context.getBean("helloService");
        service.hello();
    }
}
