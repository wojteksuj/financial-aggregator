package com.aggregator.alert.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateAlertResponse(String currencyCode, BigDecimal thresholdRate, boolean higher, boolean active,
                                  Instant createdAt
) {
}