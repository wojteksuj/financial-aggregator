package com.aggregator.market.exception;

public class RatesNotFoundException extends RuntimeException {
    public RatesNotFoundException(String message) {
        super(message);
    }
}
