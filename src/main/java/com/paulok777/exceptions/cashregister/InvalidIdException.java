package com.paulok777.exceptions.cashregister;

import com.paulok777.exceptions.CashRegisterException;

public class InvalidIdException extends CashRegisterException {
    public InvalidIdException(String message) {
        super(message);
    }
}
