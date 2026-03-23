package com.aggregator.market.service;

import com.aggregator.market.config.ExchangeRateApiConfig;
import com.aggregator.market.dto.ExchangeRateApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExchangeRateClient {
    private final ExchangeRateApiConfig exchangeRateApiConfig;

    public ExchangeRateClient(ExchangeRateApiConfig exchangeRateApiConfig) {
        this.exchangeRateApiConfig = exchangeRateApiConfig;
    }

    public ExchangeRateApiResponse fetchRates(){
        RestClient restClient = RestClient.create();

        ExchangeRateApiResponse response = restClient.get()
                .uri(exchangeRateApiConfig.getBaseUrl() + "/" + exchangeRateApiConfig.getApiKey() + "/latest/" + exchangeRateApiConfig.getBaseCurrency())
                .retrieve()
                .body(ExchangeRateApiResponse.class);
        return response;
    }





}
