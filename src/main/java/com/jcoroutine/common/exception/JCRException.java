package com.jcoroutine.common.exception;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-10
 */
public class JCRException extends Throwable {
    public JCRException() {
    }

    public JCRException(String message) {
        super(message);
    }

    public JCRException(String message, Throwable cause) {
        super(message, cause);
    }

    public JCRException(Throwable cause) {
        super(cause);
    }
}
