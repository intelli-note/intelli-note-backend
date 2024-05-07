package com.demiphea.exception.utils.network;

/**
 * FormTypeException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class FormTypeException extends RuntimeException {
    public FormTypeException() {
        super();
    }

    public FormTypeException(String message) {
        super(message);
    }

    public FormTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormTypeException(Throwable cause) {
        super(cause);
    }

    protected FormTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
