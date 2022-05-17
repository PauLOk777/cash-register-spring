package com.paulok777.exceptions.cashregister;

import com.paulok777.exceptions.CashRegisterException;

public class OrderException extends CashRegisterException {
    public OrderException(String message) {
        super(message);
    }
}
