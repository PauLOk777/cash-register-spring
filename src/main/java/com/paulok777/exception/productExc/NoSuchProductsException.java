package com.paulok777.exception.productExc;

import com.paulok777.exception.ProductException;

public class NoSuchProductsException extends ProductException {

    public NoSuchProductsException() {
    }

    public NoSuchProductsException(String message) {
        super(message);
    }

    public NoSuchProductsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchProductsException(Throwable cause) {
        super(cause);
    }
}
