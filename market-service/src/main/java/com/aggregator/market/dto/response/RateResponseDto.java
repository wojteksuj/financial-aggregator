package com.aggregator.market.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record RateResponseDto(String currencyCode, String baseCurrency, Instant timestamp, BigDecimal rate){
}
