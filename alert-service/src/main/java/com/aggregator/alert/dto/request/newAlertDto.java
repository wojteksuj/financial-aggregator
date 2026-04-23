package com.aggregator.alert.dto.request;

import java.math.BigDecimal;

public record newAlertDto(String currencyCode, BigDecimal thresholdRate, boolean higher) {
}
