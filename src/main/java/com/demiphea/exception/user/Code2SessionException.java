package com.demiphea.exception.user;

/**
 * Code2SessionException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class Code2SessionException extends RuntimeException {
    public Code2SessionException() {
        super();
    }

    public Code2SessionException(String message) {
        super(message);
    }

    public Code2SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public Code2SessionException(Throwable cause) {
        super(cause);
    }

    protected Code2SessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
