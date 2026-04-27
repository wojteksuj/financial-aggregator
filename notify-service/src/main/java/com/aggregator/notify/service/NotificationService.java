package com.aggregator.notify.service;

import com.aggregator.notify.dto.TriggeredAlertEventDto;
import com.aggregator.notify.dto.response.GetNotificationResponse;
import com.aggregator.notify.entity.Notification;
import com.aggregator.notify.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public void processAlert(TriggeredAlertEventDto eventDto) {
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

    public List<GetNotificationResponse> getNotifications(String currency) {
        List<Notification> notifications = (currency != null)
                ? notificationRepository.findByCurrencyCode(currency)
                : notificationRepository.findAll();

        return notifications.stream()
                .map(n -> new GetNotificationResponse(
                        n.getId(),
                        n.getAlertId(),
                        n.getCurrencyCode(),
                        n.getThresholdRate(),
                        n.getCurrentRate(),
                        n.getDifference(),
                        n.isHigher(),
                        n.getType(),
                        n.getTriggeredAt(),
                        n.getSentAt()
                )).toList();
    }
}
