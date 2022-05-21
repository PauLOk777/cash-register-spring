package com.paulok777.exceptions.cashregister.orders;

import com.paulok777.exceptions.CashRegisterException;

public class IllegalOrderStateException extends CashRegisterException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}
