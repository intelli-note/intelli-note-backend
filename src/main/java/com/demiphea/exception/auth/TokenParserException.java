package com.demiphea.exception.auth;

/**
 * TokenParserException
 *
 * @author demiphea
 * @since 17.0.9
 */
public class TokenParserException extends RuntimeException {
    public TokenParserException() {
        super();
    }

    public TokenParserException(String message) {
        super(message);
    }

    public TokenParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenParserException(Throwable cause) {
        super(cause);
    }

    protected TokenParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
