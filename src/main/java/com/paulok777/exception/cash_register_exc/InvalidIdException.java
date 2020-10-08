package com.paulok777.exception.cash_register_exc;

import com.paulok777.exception.CashRegisterException;

public class InvalidIdException extends CashRegisterException {
    public InvalidIdException(String message) {
        super(message);
    }
}
