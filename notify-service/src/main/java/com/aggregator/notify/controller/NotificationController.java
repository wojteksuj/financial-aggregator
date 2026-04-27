package com.aggregator.notify.controller;

import com.aggregator.notify.dto.response.GetNotificationResponse;
import com.aggregator.notify.service.NotificationService;
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

    private final NotificationService notificationService;

    @GetMapping
    public List<GetNotificationResponse> getNotifications(
            @RequestParam(required = false) String currency) {
        return notificationService.getNotifications(currency);
    }
}
