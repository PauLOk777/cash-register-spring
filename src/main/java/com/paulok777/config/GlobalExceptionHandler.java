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

    @ExceptionHandler
    public <T extends CashRegisterException> ResponseEntity<Object> orderException(T e, Locale locale) {
        return new ResponseEntity<>(messageSource.getMessage(
                e.getMessage(), null, locale), HttpStatus.BAD_REQUEST);
    }
}
