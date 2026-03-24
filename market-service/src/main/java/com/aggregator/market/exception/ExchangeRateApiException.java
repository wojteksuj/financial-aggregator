package com.aggregator.market.exception;

public class ExchangeRateApiException extends RuntimeException{
    public ExchangeRateApiException(String message) {
        super(message);
    }
}
