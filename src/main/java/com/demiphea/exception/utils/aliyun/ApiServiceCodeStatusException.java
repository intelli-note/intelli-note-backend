package com.demiphea.exception.utils.aliyun;

/**
 * ApiServiceCodeStatusException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class ApiServiceCodeStatusException extends RuntimeException {
    public ApiServiceCodeStatusException() {
        super();
    }

    public ApiServiceCodeStatusException(String message) {
        super(message);
    }

    public ApiServiceCodeStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiServiceCodeStatusException(Throwable cause) {
        super(cause);
    }

    protected ApiServiceCodeStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
