package com.paulok777.exception.cashRegisterExc.productExc;

import com.paulok777.exception.cashRegisterExc.ProductException;

public class DuplicateCodeOrNameException extends ProductException {
    public DuplicateCodeOrNameException(String message) {
        super(message);
    }
}
