package com.paulok777.exceptions.cashregister.registration;

import com.paulok777.exceptions.cashregister.RegistrationException;

public class DuplicateUsernameException extends RegistrationException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
