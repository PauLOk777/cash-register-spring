package com.paulok777.exception.cash_register_exc;

import com.paulok777.exception.CashRegisterException;

public class FieldValidationException extends CashRegisterException {
    public FieldValidationException(String message) {
        super(message);
    }
}
