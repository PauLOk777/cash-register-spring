package com.paulok777.exceptions.cashregister.products;

import com.paulok777.exceptions.CashRegisterException;

public class DuplicateCodeOrNameException extends CashRegisterException {
    public DuplicateCodeOrNameException(String message) {
        super(message);
    }
}
