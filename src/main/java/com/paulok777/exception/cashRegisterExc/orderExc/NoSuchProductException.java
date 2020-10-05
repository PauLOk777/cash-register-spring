package com.paulok777.exception.cashRegisterExc.orderExc;

import com.paulok777.exception.cashRegisterExc.OrderException;

public class NoSuchProductException extends OrderException {
    public NoSuchProductException(String message) {
        super(message);
    }
}
