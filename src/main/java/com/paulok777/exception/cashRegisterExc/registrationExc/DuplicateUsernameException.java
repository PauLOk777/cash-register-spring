package com.paulok777.exception.cashRegisterExc.registrationExc;

import com.paulok777.exception.cashRegisterExc.RegistrationException;

public class DuplicateUsernameException extends RegistrationException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
