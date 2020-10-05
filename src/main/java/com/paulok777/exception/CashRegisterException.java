package com.paulok777.exception;

public class CashRegisterException extends RuntimeException {
    public CashRegisterException() {
    }

    public CashRegisterException(String message) {
        super(message);
    }

    public CashRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CashRegisterException(Throwable cause) {
        super(cause);
    }
}
