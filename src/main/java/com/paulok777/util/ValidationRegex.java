package com.paulok777.util;

public class ValidationRegex {
    public static final String FIRST_NAME_REGEX = "^[^\\d\\s]{2,60}$";
    public static final String LAST_NAME_REGEX = "^[^\\d\\s]{2,60}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    public static final String USERNAME_REGEX = "^[^\\s]{5,20}$";
    public static final String PASSWORD_REGEX = "^[-=+_a-zA-Z\\d]{4,}$";
    public static final String PHONE_NUMBER_REGEX = "[-+_\\d]{9,13}";
    public static final String POSITION_REGEX = "^(CASHIER)|^(SENIOR_CASHIER)$|^(COMMODITY_EXPERT)$";
    public static final String CODE_REGEX = "^[a-zA-Z0-9]{4,}$";
    public static final String PRODUCT_NAME_REGEX = "^[\\s\\S]+$";
    public static final String MEASURE_REGEXP = "^(BY_QUANTITY)$|^(BY_WEIGHT)$";
}
