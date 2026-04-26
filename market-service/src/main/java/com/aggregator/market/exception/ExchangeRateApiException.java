package com.aggregator.market.exception;

public class ExchangeRateApiException extends RuntimeException {
    public ExchangeRateApiException(String ratesResult) {
        super("external api error: " + ratesResult);
    }
}
