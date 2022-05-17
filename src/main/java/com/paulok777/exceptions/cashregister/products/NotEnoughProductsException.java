package com.paulok777.exceptions.cashregister.products;

import com.paulok777.exceptions.cashregister.ProductException;

public class NotEnoughProductsException extends ProductException {
    public NotEnoughProductsException(String message) {
        super(message);
    }
}
