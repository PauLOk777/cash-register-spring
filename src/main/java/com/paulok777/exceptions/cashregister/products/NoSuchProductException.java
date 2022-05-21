package com.paulok777.exceptions.cashregister.products;

import com.paulok777.exceptions.CashRegisterException;

public class NoSuchProductException extends CashRegisterException {
    public NoSuchProductException(String message) {
        super(message);
    }
}
