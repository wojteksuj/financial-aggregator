package com.aggregator.market.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateApiResponse(
        @JsonProperty("result") String result,
        @JsonProperty("base_code") String baseCode,
        @JsonProperty("conversion_rates") Map<String, BigDecimal> conversionRates
)
{}
