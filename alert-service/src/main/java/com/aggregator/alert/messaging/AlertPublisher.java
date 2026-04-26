package com.aggregator.alert.messaging;

import com.aggregator.alert.config.RabbitConfig;
import com.aggregator.alert.dto.TriggeredAlertEventDto;
import com.aggregator.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AlertPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishTriggeredAlert(Alert alert, BigDecimal currentRate) {
        TriggeredAlertEventDto event = new TriggeredAlertEventDto(
                alert.getId(),
                alert.getCurrencyCode(),
                alert.getThresholdRate(),
                currentRate,
                alert.isHigher(),
                Instant.now()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.ALERT_EXCHANGE,
                RabbitConfig.ALERT_TRIGGERED_QUEUE,
                event
        );
    }
}