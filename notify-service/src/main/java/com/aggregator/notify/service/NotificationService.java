package com.aggregator.notify.service;

import com.aggregator.notify.dto.TriggeredAlertEventDto;
import com.aggregator.notify.entity.Notification;
import com.aggregator.notify.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public void processAlert(TriggeredAlertEventDto eventDto){
        emailService.sendAlertEmail(eventDto);

        Notification newNotification = new Notification(
                eventDto.alertId(),
                eventDto.currencyCode(),
                eventDto.thresholdRate(),
                eventDto.currentRate(),
                eventDto.higher(),
                "EMAIL",
                eventDto.triggeredAt()
        );
        notificationRepository.save(newNotification);
    }

}
