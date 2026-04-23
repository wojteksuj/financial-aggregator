package com.aggregator.alert.dto.request;

import java.math.BigDecimal;

public record NewAlertDto(String currencyCode, BigDecimal thresholdRate, boolean higher) {
}
