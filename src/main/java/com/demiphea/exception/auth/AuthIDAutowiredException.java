package com.demiphea.exception.auth;

/**
 * AuthIDAutowiredException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class AuthIDAutowiredException extends RuntimeException {
    public AuthIDAutowiredException() {
        super();
    }

    public AuthIDAutowiredException(String message) {
        super(message);
    }

    public AuthIDAutowiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthIDAutowiredException(Throwable cause) {
        super(cause);
    }

    protected AuthIDAutowiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
