package com.paulok777.configs;

import com.paulok777.exceptions.CashRegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String GLOBAL_MESSAGE_WARNING = "globalWarningMessage";

    private final MessageSource messageSource;

    @ExceptionHandler
    public <T extends CashRegisterException> String orderException(T e, Locale locale, RedirectAttributes attributes) {
        String message = messageSource.getMessage(e.getMessage(), null, locale);
        attributes.addFlashAttribute(GLOBAL_MESSAGE_WARNING, message);
        return e.getRedirectUrl();
    }
}
