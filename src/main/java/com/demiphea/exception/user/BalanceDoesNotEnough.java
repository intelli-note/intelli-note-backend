package com.demiphea.exception.user;

/**
 * BalanceDoesNotEnough
 *
 * @author demiphea
 * @since 17.0.9
 */
public class BalanceDoesNotEnough extends RuntimeException {
    public BalanceDoesNotEnough() {
        super();
    }

    public BalanceDoesNotEnough(String message) {
        super(message);
    }

    public BalanceDoesNotEnough(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceDoesNotEnough(Throwable cause) {
        super(cause);
    }

    protected BalanceDoesNotEnough(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
