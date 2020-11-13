package com.utils.enums;

public enum AccountCurrency {
    AMD("AMD"),
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private final String currency;

    AccountCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}

