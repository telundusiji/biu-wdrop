package site.teamo.biu.wdrop.base.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/5
 */
public class ShutdownProcessorException extends RuntimeException {
    public ShutdownProcessorException() {
        super();
    }

    public ShutdownProcessorException(String message) {
        super(message);
    }

    public ShutdownProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShutdownProcessorException(Throwable cause) {
        super(cause);
    }

    protected ShutdownProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
