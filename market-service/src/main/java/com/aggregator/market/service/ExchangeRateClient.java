package com.aggregator.market.service;

import com.aggregator.market.config.ExchangeRateApiConfig;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateClient {
    private final ExchangeRateApiConfig exchangeRateApiConfig;

    public ExchangeRateClient(ExchangeRateApiConfig exchangeRateApiConfig) {
        this.exchangeRateApiConfig = exchangeRateApiConfig;
    }


}
