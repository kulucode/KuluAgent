package com.tpson.kuluagent.exception;

/**
 * Created by Zhangka in 2018/04/19
 */
public class ParamRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -1359511522887055269L;

    /**
     *
     */
    public ParamRuntimeException() {
        super();
    }

    /**
     *
     * @param message
     */
    public ParamRuntimeException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ParamRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public ParamRuntimeException(Throwable cause) {
        super(cause);
    }
}

