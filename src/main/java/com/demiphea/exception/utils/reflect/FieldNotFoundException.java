package com.demiphea.exception.utils.reflect;

/**
 * FieldNotFoundException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldNotFoundException(Throwable cause) {
        super(cause);
    }

    protected FieldNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
