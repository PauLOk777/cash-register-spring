package com.paulok777.config;

import com.paulok777.exception.CashRegisterException;
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
    private static final String GO_BACK = "goBack";

    @ExceptionHandler
    public <T extends CashRegisterException> ResponseEntity<Object> orderException(T e, Locale locale) {
        String message = messageSource.getMessage(e.getMessage(), null, locale) +
                System.lineSeparator() + messageSource.getMessage(GO_BACK, null, locale);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
