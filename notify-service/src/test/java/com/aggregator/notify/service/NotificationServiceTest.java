package com.aggregator.notify.service;

import com.aggregator.notify.dto.TriggeredAlertEventDto;
import com.aggregator.notify.dto.response.GetNotificationResponse;
import com.aggregator.notify.entity.Notification;
import com.aggregator.notify.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.aggregator.notify.exception.CurrencyCodeNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private TriggeredAlertEventDto event;
    private Notification notification;

    @BeforeEach
    void setUp() {
        event = new TriggeredAlertEventDto(
                1L,
                "EUR",
                new BigDecimal("4.30"),
                new BigDecimal("4.35"),
                true,
                Instant.parse("2026-04-27T10:00:00Z")
        );

        notification = new Notification(
                1L,
                "EUR",
                new BigDecimal("4.30"),
                new BigDecimal("4.35"),
                true,
                "EMAIL",
                Instant.parse("2026-04-27T10:00:00Z")
        );
    }

    @Test
    void processAlert_sendsEmailAndSavesNotification() {
        notificationService.processAlert(event);

        verify(emailService).sendAlertEmail(event);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void processAlert_savesNotificationWithCorrectFields() {
        notificationService.processAlert(event);

        verify(notificationRepository).save(argThat(n ->
                n.getAlertId().equals(1L) &&
                n.getCurrencyCode().equals("EUR") &&
                n.getThresholdRate().compareTo(new BigDecimal("4.30")) == 0 &&
                n.getCurrentRate().compareTo(new BigDecimal("4.35")) == 0 &&
                n.isHigher() &&
                n.getType().equals("EMAIL")
        ));
    }

    @Test
    void getNotifications_returnsAllWhenNoCurrencyProvided() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        List<GetNotificationResponse> result = notificationService.getNotifications(null);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().currencyCode()).isEqualTo("EUR");
        verify(notificationRepository).findAll();
        verify(notificationRepository, never()).findByCurrencyCode(any());
    }

    @Test
    void getNotifications_filtersByCurrencyWhenProvided() {
        when(notificationRepository.findByCurrencyCode("EUR")).thenReturn(List.of(notification));

        List<GetNotificationResponse> result = notificationService.getNotifications("EUR");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().currencyCode()).isEqualTo("EUR");
        verify(notificationRepository).findByCurrencyCode("EUR");
        verify(notificationRepository, never()).findAll();
    }

    @Test
    void getNotifications_throwsWhenCurrencyNotFound() {
        when(notificationRepository.findByCurrencyCode("USD")).thenReturn(List.of());

        assertThatThrownBy(() -> notificationService.getNotifications("USD"))
                .isInstanceOf(CurrencyCodeNotFoundException.class)
                .hasMessageContaining("USD");
    }
}
