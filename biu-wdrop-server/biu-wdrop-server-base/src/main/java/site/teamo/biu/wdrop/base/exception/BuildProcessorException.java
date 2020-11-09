package site.teamo.biu.wdrop.base.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/5
 */
public class BuildProcessorException extends RuntimeException {
    public BuildProcessorException() {
        super();
    }

    public BuildProcessorException(String message) {
        super(message);
    }

    public BuildProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildProcessorException(Throwable cause) {
        super(cause);
    }

    protected BuildProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
