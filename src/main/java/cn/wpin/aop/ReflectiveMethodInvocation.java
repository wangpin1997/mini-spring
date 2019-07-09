package cn.wpin.aop;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class ReflectiveMethodInvocation implements MethodInvocation {

	protected Object target;

    protected Method method;

    protected Object[] arguments;

	public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
		this.target = target;
		this.method = method;
		this.arguments = arguments;
	}
	public Method getMethod() {
		return method;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object proceed() throws Throwable {
		return method.invoke(target, arguments);
	}


	public Object getThis() {
		return target;
	}


	public AccessibleObject getStaticPart() {
		return method;
	}
}