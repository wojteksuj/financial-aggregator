package com.aggregator.market.exception;

public class RatesNotFoundException extends RuntimeException {
    public RatesNotFoundException() {
        super("rates not found for provided base code");
    }
}
