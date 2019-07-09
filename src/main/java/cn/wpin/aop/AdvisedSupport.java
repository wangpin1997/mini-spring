package cn.wpin.aop;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author wangpin
 */
@Data
public class AdvisedSupport {

    private TargetSource targetSource;

    private MethodInterceptor methodInterceptor;

    private MethodMatcher methodMatcher;
}
