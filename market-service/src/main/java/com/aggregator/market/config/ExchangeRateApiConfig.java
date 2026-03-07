package com.aggregator.market.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.exchangerate")
public class ExchangeRateApiConfig {
    private String apiKey;
    private String baseUrl;
    private String baseCurrency;
}
