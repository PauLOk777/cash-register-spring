package com.paulok777.exception.cashRegisterExc.orderExc;

import com.paulok777.exception.cashRegisterExc.OrderException;

public class IllegalOrderStateException extends OrderException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}
