package com.paulok777.exceptions.cashregister.orders;

import com.paulok777.exceptions.cashregister.OrderException;

public class NoSuchProductException extends OrderException {
    public NoSuchProductException(String message) {
        super(message);
    }
}
