package com.demiphea.exception.common;

/**
 * ObjectDoesNotExistException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class ObjectDoesNotExistException extends RuntimeException {
    public ObjectDoesNotExistException() {
        super();
    }

    public ObjectDoesNotExistException(String message) {
        super(message);
    }

    public ObjectDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectDoesNotExistException(Throwable cause) {
        super(cause);
    }

    protected ObjectDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
