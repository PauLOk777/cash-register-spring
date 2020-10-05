package com.paulok777.exception.orderExc;

import com.paulok777.exception.OrderException;

public class IllegalOrderStateException extends OrderException {

    public IllegalOrderStateException() {
    }

    public IllegalOrderStateException(String message) {
        super(message);
    }

    public IllegalOrderStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalOrderStateException(Throwable cause) {
        super(cause);
    }
}
