package com.tpson.kuluagent.exception;

/**
 * Created by Zhangka in 2018/04/19
 */
public class RepeatRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3528121065272519718L;

    /**
     *
     */
    public RepeatRuntimeException() {
        super();
    }

    /**
     *
     * @param message
     */
    public RepeatRuntimeException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public RepeatRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public RepeatRuntimeException(Throwable cause) {
        super(cause);
    }
}

