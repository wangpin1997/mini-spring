package cn.wpin.aop;

public class ProxyFactory extends AdvisedSupport implements AopProxy{

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    protected final AopProxy createAopProxy() {
        return new CglibAopProxy(this);
    }
}
