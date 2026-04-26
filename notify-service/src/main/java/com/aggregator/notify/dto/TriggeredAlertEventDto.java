package com.aggregator.notify.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TriggeredAlertEventDto(
        Long alertId,
        String currencyCode,
        BigDecimal thresholdRate,
        BigDecimal currentRate,
        boolean higher,
        Instant triggeredAt
) {}
