package com.paulok777.exception.productExc;

import com.paulok777.exception.ProductException;

public class DuplicateCodeOrNameException extends ProductException {
    public DuplicateCodeOrNameException() {
    }

    public DuplicateCodeOrNameException(String message) {
        super(message);
    }

    public DuplicateCodeOrNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCodeOrNameException(Throwable cause) {
        super(cause);
    }
}
