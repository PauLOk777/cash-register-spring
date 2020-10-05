package com.paulok777.exception.cashRegisterExc;

import com.paulok777.exception.CashRegisterException;

public class InvalidIdException extends CashRegisterException {
    public InvalidIdException(String message) {
        super(message);
    }
}
