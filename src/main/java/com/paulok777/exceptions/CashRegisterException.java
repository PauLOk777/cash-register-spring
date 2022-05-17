package com.paulok777.exceptions;

public class CashRegisterException extends RuntimeException {

    private String redirectUrl;

    public CashRegisterException(String message) {
        super(message);
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
