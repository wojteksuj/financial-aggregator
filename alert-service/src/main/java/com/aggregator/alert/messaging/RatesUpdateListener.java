package com.aggregator.alert.messaging;

import com.aggregator.alert.config.RabbitConfig;
import com.aggregator.alert.entity.Alert;
import com.aggregator.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatesUpdateListener {

    private final AlertService alertService;
    private final AlertPublisher alertPublisher;

    @RabbitListener(queues = RabbitConfig.RATES_UPDATED_QUEUE)
    public void handleRatesUpdate(Map<String, BigDecimal> currencyRates) {
        log.info("Received rates update: {}", currencyRates);
        
        for (Map.Entry<String, BigDecimal> entry : currencyRates.entrySet()) {
            String currencyCode = entry.getKey();
            BigDecimal currentRate = entry.getValue();
            
            List<Alert> triggeredAlerts = alertService.checkAlerts(currencyCode, currentRate);
            
            if (!triggeredAlerts.isEmpty()) {
                log.info("Found {} triggered alerts for currency {}", 
                        triggeredAlerts.size(), currencyCode);
                
                for (Alert alert : triggeredAlerts) {
                    alertPublisher.publishTriggeredAlert(alert);
                }
            }
        }
    }
}