package com.paulok777.exceptions.cashregister.orders;

import com.paulok777.exceptions.cashregister.OrderException;

public class IllegalOrderStateException extends OrderException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}
