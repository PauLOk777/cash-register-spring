package com.paulok777.configs;

import com.paulok777.exceptions.CashRegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String GLOBAL_MESSAGE_WARNING = "globalWarningMessage";
    private static final String DEFAULT_GLOBAL_MESSAGE = "default.global.message";

    private final MessageSource messageSource;

    @ExceptionHandler
    public <T extends CashRegisterException> String orderException(T orderException, Locale locale, RedirectAttributes attributes) {
        String message;
        try {
            message = messageSource.getMessage(orderException.getMessage(), null, locale);
        } catch (NoSuchMessageException noSuchMessageException) {
            message = messageSource.getMessage(DEFAULT_GLOBAL_MESSAGE, null, locale);
        }
        attributes.addFlashAttribute(GLOBAL_MESSAGE_WARNING, message);
        return orderException.getRedirectUrl();
    }
}
