package cn.wpin.aop;

import cn.wpin.business.HelloService;
import cn.wpin.business.HelloServiceImpl;
import cn.wpin.business.OrderService;
import cn.wpin.context.ApplicationContext;
import cn.wpin.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * jdk动态代理测试类
 */
public class JdkDynamicAopProxyTest {

    @Test
    public void main() throws Exception {
        ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
        HelloService service= (HelloService) context.getBean("helloService");
        service.hello();

        //设置被代理对象（切入点）
        AdvisedSupport advisedSupport=new AdvisedSupport();
        TargetSource targetSource=new TargetSource(HelloServiceImpl.class,service,HelloService.class, OrderService.class);
        advisedSupport.setTargetSource(targetSource);

        //设置拦截器（advice）
        TimeInterceptor timeInterceptor=new TimeInterceptor();
        advisedSupport.setMethodInterceptor(timeInterceptor);

        //创建代理
        JdkDynamicAopProxy jdkDynamicAopProxy=new JdkDynamicAopProxy(advisedSupport);
        HelloService helloService= (HelloService) jdkDynamicAopProxy.getProxy();
        OrderService orderService= (OrderService) jdkDynamicAopProxy.getProxy();

        //调用生成代理类，运行方法
        helloService.hello();
//        orderService.print();
    }
}
