package com.aggregator.notify.exception;

public class CurrencyCodeNotFoundException extends RuntimeException {
    public CurrencyCodeNotFoundException(String currencyCode) {
        super("no notifications found for currency code: " + currencyCode);
    }
}
