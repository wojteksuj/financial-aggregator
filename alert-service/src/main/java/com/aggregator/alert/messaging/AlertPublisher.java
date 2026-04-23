package com.aggregator.alert.messaging;

import com.aggregator.alert.config.RabbitConfig;
import com.aggregator.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishTriggeredAlert(Alert alert) {
        TriggeredAlertEvent event = new TriggeredAlertEvent(
                alert.getId(),
                alert.getCurrencyCode(),
                alert.getThresholdRate(),
                alert.isHigher(),
                Instant.now()
        );

        log.info("Publishing triggered alert: {}", event);
        
        rabbitTemplate.convertAndSend(
                RabbitConfig.ALERT_EXCHANGE,
                RabbitConfig.ALERT_TRIGGERED_QUEUE,
                event
        );
    }

    public record TriggeredAlertEvent(
            Long alertId,
            String currencyCode,
            BigDecimal thresholdRate,
            boolean higher,
            Instant triggeredAt
    ) {}
}