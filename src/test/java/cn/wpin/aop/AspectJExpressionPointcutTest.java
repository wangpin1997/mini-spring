package cn.wpin.aop;

import cn.wpin.aop.aspect.AspectJExpressionPointcut;
import cn.wpin.business.HelloService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 切面匹配测试
 */
public class AspectJExpressionPointcutTest {

    @Test
    public void test(){
        String expression ="execution(* cn.wpin.business.*.*(..))";
        AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        boolean flag=pointcut.getClassFilter().matches(HelloService.class);
        Assert.assertTrue(flag);
    }
}
