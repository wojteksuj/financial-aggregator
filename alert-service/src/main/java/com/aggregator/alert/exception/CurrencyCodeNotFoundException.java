package com.aggregator.alert.exception;

public class CurrencyCodeNotFoundException extends RuntimeException {
    public CurrencyCodeNotFoundException(String currencyCode) {
        super("no alerts found for currency code: " + currencyCode);
    }
}
