package com.paulok777.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CASHIER,
    SENIOR_CASHIER,
    COMMODITY_EXPERT;

    @Override
    public String getAuthority() {
        return name();
    }
}
