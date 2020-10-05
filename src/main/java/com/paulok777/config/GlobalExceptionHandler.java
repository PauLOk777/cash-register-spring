package com.paulok777.config;

import com.paulok777.exception.*;
import com.paulok777.exception.orderExc.NoSuchProductException;
import com.paulok777.exception.productExc.DuplicateCodeOrNameException;
import com.paulok777.exception.productExc.NotEnoughProductsException;
import com.paulok777.exception.registrationExc.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler({IllegalArgumentException.class, NoSuchProductException.class})
    public <T extends OrderException> ResponseEntity<Object> orderException(T e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicateCodeOrNameException.class, NotEnoughProductsException.class})
    public <T extends ProductException> ResponseEntity<Object> productException(T e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicateUsernameException.class})
    public <T extends RegistrationException> ResponseEntity<Object> registrationException(T e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<Object> invalidIdException(InvalidIdException e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<Object> filedValidationException(FieldValidationException e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }
}
