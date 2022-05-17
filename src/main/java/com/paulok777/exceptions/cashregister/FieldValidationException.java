package com.paulok777.exceptions.cashregister;

import com.paulok777.exceptions.CashRegisterException;

public class FieldValidationException extends CashRegisterException {
    public FieldValidationException(String message) {
        super(message);
    }
}
