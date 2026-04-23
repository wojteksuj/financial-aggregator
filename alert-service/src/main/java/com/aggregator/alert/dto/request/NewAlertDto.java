package com.aggregator.alert.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NewAlertDto(
        @NotBlank(message = "Currency code is required")
        String currencyCode,
        
        @NotNull(message = "Threshold rate is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Threshold rate must be positive")
        BigDecimal thresholdRate,
        
        @NotNull(message = "Higher flag is required")
        Boolean higher
) {
}
