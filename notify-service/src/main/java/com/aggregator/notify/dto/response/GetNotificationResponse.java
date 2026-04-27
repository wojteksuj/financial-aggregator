package com.aggregator.notify.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record GetNotificationResponse(
        Long id,
        Long alertId,
        String currencyCode,
        BigDecimal thresholdRate,
        BigDecimal currentRate,
        BigDecimal difference,
        boolean higher,
        String type,
        Instant triggeredAt,
        Instant sentAt
) {}
