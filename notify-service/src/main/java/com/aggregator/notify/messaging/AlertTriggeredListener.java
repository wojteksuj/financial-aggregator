package com.aggregator.notify.messaging;

import com.aggregator.notify.config.RabbitConfig;
import com.aggregator.notify.dto.TriggeredAlertEventDto;
import com.aggregator.notify.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertTriggeredListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.ALERT_TRIGGERED_QUEUE)
    public void handleAlertTrigger(TriggeredAlertEventDto eventDto){
        notificationService.processAlert(eventDto);
    }
}
