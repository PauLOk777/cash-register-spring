package com.paulok777.exception.productExc;

import com.paulok777.exception.ProductException;

public class NotEnoughProductsException extends ProductException {
    public NotEnoughProductsException() {
    }

    public NotEnoughProductsException(String message) {
        super(message);
    }

    public NotEnoughProductsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughProductsException(Throwable cause) {
        super(cause);
    }
}
