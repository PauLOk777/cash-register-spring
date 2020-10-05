package com.paulok777.exception;

public class FieldValidationException extends RuntimeException {
    public FieldValidationException() {
    }

    public FieldValidationException(String message) {
        super(message);
    }

    public FieldValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldValidationException(Throwable cause) {
        super(cause);
    }
}
