package com.aggregator.alert.service;
import com.aggregator.alert.entity.Alert;
import com.aggregator.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<Alert> checkAlerts(String currencyCode, BigDecimal currentRate) {
        List<Alert> activeAlerts = alertRepository.findAlertByCurrencyCodeAndActiveIsTrue(currencyCode);

        List<Alert> triggeredAlerts = activeAlerts.stream()
                .filter(alert -> isThresholdCrossed(alert, currentRate))
                .toList();

        if (!triggeredAlerts.isEmpty()) {
            triggeredAlerts.forEach(alert -> alert.setActive(false));
            alertRepository.saveAll(triggeredAlerts);
        }

        return triggeredAlerts;
    }

    private boolean isThresholdCrossed(Alert alert, BigDecimal currentRate) {
        int comparison = currentRate.compareTo(alert.getThresholdRate());

        if (alert.isHigher()) {
            return comparison > 0;
        } else {
            return comparison < 0;
        }
    }
}