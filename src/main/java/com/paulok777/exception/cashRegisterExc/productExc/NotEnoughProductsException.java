package com.paulok777.exception.cashRegisterExc.productExc;

import com.paulok777.exception.cashRegisterExc.ProductException;

public class NotEnoughProductsException extends ProductException {
    public NotEnoughProductsException(String message) {
        super(message);
    }
}
