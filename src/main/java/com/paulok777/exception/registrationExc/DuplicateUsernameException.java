package com.paulok777.exception.registrationExc;

import com.paulok777.exception.RegistrationException;

public class DuplicateUsernameException extends RegistrationException {
    public DuplicateUsernameException() {
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }
}
