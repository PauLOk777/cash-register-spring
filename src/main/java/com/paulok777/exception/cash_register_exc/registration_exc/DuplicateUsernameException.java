package com.paulok777.exception.cash_register_exc.registration_exc;

import com.paulok777.exception.cash_register_exc.RegistrationException;

public class DuplicateUsernameException extends RegistrationException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
