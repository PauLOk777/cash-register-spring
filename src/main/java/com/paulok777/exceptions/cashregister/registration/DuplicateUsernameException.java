package com.paulok777.exceptions.cashregister.registration;

import com.paulok777.exceptions.CashRegisterException;

public class DuplicateUsernameException extends CashRegisterException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
