package com.paulok777.exception.cash_register_exc.order_exc;

import com.paulok777.exception.cash_register_exc.OrderException;

public class IllegalOrderStateException extends OrderException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}
