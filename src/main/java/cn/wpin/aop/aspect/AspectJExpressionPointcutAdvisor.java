package cn.wpin.aop.aspect;

import cn.wpin.aop.Pointcut;
import cn.wpin.aop.PointcutAdvisor;
import lombok.Data;
import org.aopalliance.aop.Advice;

/**
 * @author wangpin
 */
@Data
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    private Advice advice;

    public Pointcut getPointcut() {
        return pointcut;
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setExpression(String expression) {
        this.pointcut.setExpression(expression);
    }
}
