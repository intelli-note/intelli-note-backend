package com.demiphea.exception.utils.aliyun;

/**
 * ApiResponseCodeStatusException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class ApiResponseCodeStatusException extends RuntimeException {
    public ApiResponseCodeStatusException() {
        super();
    }

    public ApiResponseCodeStatusException(String message) {
        super(message);
    }

    public ApiResponseCodeStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiResponseCodeStatusException(Throwable cause) {
        super(cause);
    }

    protected ApiResponseCodeStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
