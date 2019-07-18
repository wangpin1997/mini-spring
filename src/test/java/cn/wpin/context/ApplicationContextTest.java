package cn.wpin.context;

import cn.wpin.bean.User;
import cn.wpin.business.HelloService;
import org.junit.Test;

public class ApplicationContextTest {


    @Test
    public void test() throws Exception {
//        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
//        HelloService service= (HelloService) context.getBean("helloService");
//        service.hello();

        //基于注解实现的IOC
        AnnotationConfigApplicationContext context1=new AnnotationConfigApplicationContext();
        context1.register(User.class);
        context1.refresh();
        User user= (User) context1.getBean("User");
        user.test();
    }
}
