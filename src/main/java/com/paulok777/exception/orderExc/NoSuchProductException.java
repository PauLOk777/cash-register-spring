package com.paulok777.exception.orderExc;

import com.paulok777.exception.OrderException;

public class NoSuchProductException extends OrderException {
    public NoSuchProductException() {
    }

    public NoSuchProductException(String message) {
        super(message);
    }

    public NoSuchProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchProductException(Throwable cause) {
        super(cause);
    }
}
