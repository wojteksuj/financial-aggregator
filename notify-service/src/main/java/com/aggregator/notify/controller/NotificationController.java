package com.aggregator.notify.controller;

import com.aggregator.notify.dto.response.GetNotificationResponse;
import com.aggregator.notify.entity.Notification;
import com.aggregator.notify.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<GetNotificationResponse> getNotifications(
            @RequestParam(required = false) String currency) {

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
