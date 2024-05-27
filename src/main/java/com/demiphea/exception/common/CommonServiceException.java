package com.demiphea.exception.common;

/**
 * CommonServiceException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class CommonServiceException extends RuntimeException {
    public CommonServiceException() {
        super();
    }

    public CommonServiceException(String message) {
        super(message);
    }

    public CommonServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonServiceException(Throwable cause) {
        super(cause);
    }

    protected CommonServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
