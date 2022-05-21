package com.paulok777.exceptions.cashregister.products;

import com.paulok777.exceptions.CashRegisterException;

public class NotEnoughProductsException extends CashRegisterException {
    public NotEnoughProductsException(String message) {
        super(message);
    }
}
