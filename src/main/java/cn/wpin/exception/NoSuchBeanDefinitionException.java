package cn.wpin.exception;

/**
 * bean找不到异常
 * @author wangpin
 */
public class NoSuchBeanDefinitionException extends BeansException {

    private final String beanName;



    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param name the name of the missing bean
     */
    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param name the name of the missing bean
     * @param message detailed message describing the problem
     */
    public NoSuchBeanDefinitionException(String name, String message) {
        super("No bean named '" + name + "' available: " + message);
        this.beanName=name;
    }



    /**
     * Return the name of the missing bean, if it was a lookup <em>by name</em> that failed.
     */
    public String getBeanName() {
        return this.beanName;
    }



}
