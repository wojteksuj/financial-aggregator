package com.aggregator.alert.messaging;

import com.aggregator.alert.config.RabbitConfig;
import com.aggregator.alert.entity.Alert;
import com.aggregator.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RatesUpdateListener {

    private final AlertService alertService;
    private final AlertPublisher alertPublisher;

    @RabbitListener(queues = RabbitConfig.RATES_UPDATED_QUEUE)
    public void handleRatesUpdate(Map<String, BigDecimal> currencyRates) {
        for (Map.Entry<String, BigDecimal> entry : currencyRates.entrySet()) {
            String currencyCode = entry.getKey();
            BigDecimal currentRate = entry.getValue();
            
            List<Alert> triggeredAlerts = alertService.checkAlerts(currencyCode, currentRate);
            
            if (!triggeredAlerts.isEmpty()) {
                for (Alert alert : triggeredAlerts) {
                    alertPublisher.publishTriggeredAlert(alert);
                }
            }
        }
    }
}