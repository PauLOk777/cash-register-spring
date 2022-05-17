package com.paulok777.exceptions.cashregister.products;

import com.paulok777.exceptions.cashregister.ProductException;

public class DuplicateCodeOrNameException extends ProductException {
    public DuplicateCodeOrNameException(String message) {
        super(message);
    }
}
