package cn.wpin.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangpin
 */
@Data
@AllArgsConstructor
public abstract class AbstractAopProxy implements AopProxy {

    protected AdvisedSupport advisedSupport;

}
