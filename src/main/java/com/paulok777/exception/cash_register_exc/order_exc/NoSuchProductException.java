package com.paulok777.exception.cash_register_exc.order_exc;

import com.paulok777.exception.cash_register_exc.OrderException;

public class NoSuchProductException extends OrderException {
    public NoSuchProductException(String message) {
        super(message);
    }
}
