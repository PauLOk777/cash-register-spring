package com.paulok777.utils;

import com.paulok777.dto.ProductDto;
import com.paulok777.dto.UserDto;
import com.paulok777.exceptions.cashregister.FieldValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class Validator {
    public static void validateUser(UserDto userDTO) {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!userDTO.getFirstName().matches(ValidationRegex.FIRST_NAME_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_FIRST_NAME);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_FIRST_NAME);
        }

        if (!userDTO.getLastName().matches(ValidationRegex.LAST_NAME_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_LAST_NAME);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_LAST_NAME);
        }

        if (!userDTO.getEmail().matches(ValidationRegex.EMAIL_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_EMAIL);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_EMAIL);
        }

        if (!userDTO.getUsername().matches(ValidationRegex.USERNAME_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_USERNAME);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_USERNAME);
        }

        if (!userDTO.getPassword().matches(ValidationRegex.PASSWORD_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_PASSWORD);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_PASSWORD);
        }

        if (!userDTO.getPhoneNumber().matches(ValidationRegex.PHONE_NUMBER_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_PHONE_NUMBER);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_PHONE_NUMBER);
        }

        if (!userDTO.getRole().matches(ValidationRegex.POSITION_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_USER_POSITION);
            throw new FieldValidationException(ExceptionKeys.INVALID_USER_POSITION);
        }
    }

    public static void validateProduct(ProductDto productDTO) {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!productDTO.getCode().matches(ValidationRegex.CODE_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_PRODUCT_CODE);
            throw new FieldValidationException(ExceptionKeys.INVALID_PRODUCT_CODE);
        }

        if (!productDTO.getName().trim().matches(ValidationRegex.PRODUCT_NAME_REGEX)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_PRODUCT_NAME);
            throw new FieldValidationException(ExceptionKeys.INVALID_PRODUCT_NAME);
        }

        if (productDTO.getPrice() < 1) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_PRODUCT_PRICE);
            throw new FieldValidationException(ExceptionKeys.INVALID_PRODUCT_PRICE);
        }

        validateAmountForCommodityExpert(productDTO.getAmount());

        if (!productDTO.getMeasure().matches(ValidationRegex.MEASURE_REGEXP)) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_PRODUCT_MEASURE);
            throw new FieldValidationException(ExceptionKeys.INVALID_PRODUCT_MEASURE);
        }
    }

    public static void validateAmountForCommodityExpert(Long amount) {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (amount < 0) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_AMOUNT_COMMODITY_EXPERT);
            throw new FieldValidationException(ExceptionKeys.INVALID_AMOUNT_COMMODITY_EXPERT);
        }
    }

    public static void validateAmountForCashier(Long amount) {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (amount < 1) {
            log.error("(username: {}) {}.", currentUsername, ExceptionKeys.INVALID_AMOUNT_CASHIER);
            throw new FieldValidationException(ExceptionKeys.INVALID_AMOUNT_CASHIER);
        }
    }
}
