package com.paulok777.exception.cashRegisterExc.productExc;

import com.paulok777.exception.cashRegisterExc.ProductException;

public class NoSuchProductsException extends ProductException {
    public NoSuchProductsException(String message) {
        super(message);
    }
}
