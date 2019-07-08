package cn.wpin.exception;

/**
 * 异常处理上层接口
 * @author wangpin
 */
public abstract class BeansException extends RuntimeException {
    /**
     * Create a new BeansException with the specified message.
     * @param msg the detail message
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
