package com.aggregator.market.entity.enumeration;

public enum Currency {
    EURO("EUR"),
    UNITED_STATES_DOLLAR("USD"),
    POUND_STERLING("GBP"),
    SWISS_FRANC("CHF"),
    CZECH_CROWN("CZK"),
    JAPANESE_YEN("JPY");

    private final String currencyCode;

    Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
