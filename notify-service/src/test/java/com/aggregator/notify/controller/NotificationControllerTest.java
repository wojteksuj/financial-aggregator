package com.aggregator.notify.controller;

import com.aggregator.notify.dto.response.GetNotificationResponse;
import com.aggregator.notify.exception.CurrencyCodeNotFoundException;
import com.aggregator.notify.exception.GlobalExceptionHandler;
import com.aggregator.notify.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {NotificationController.class, GlobalExceptionHandler.class})
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void getNotifications_returnsOkWithAllNotifications() throws Exception {
        GetNotificationResponse response = new GetNotificationResponse(
                1L, 1L, "EUR",
                new BigDecimal("4.30"), new BigDecimal("4.35"), new BigDecimal("0.05"),
                true, "EMAIL",
                Instant.parse("2026-04-27T10:00:00Z"),
                Instant.parse("2026-04-27T10:00:01Z")
        );
        when(notificationService.getNotifications(null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("EUR"))
                .andExpect(jsonPath("$[0].type").value("EMAIL"))
                .andExpect(jsonPath("$[0].thresholdRate").value(4.30));
    }

    @Test
    void getNotifications_returnsOkWithFilteredNotifications() throws Exception {
        GetNotificationResponse response = new GetNotificationResponse(
                1L, 1L, "EUR",
                new BigDecimal("4.30"), new BigDecimal("4.35"), new BigDecimal("0.05"),
                true, "EMAIL",
                Instant.parse("2026-04-27T10:00:00Z"),
                Instant.parse("2026-04-27T10:00:01Z")
        );
        when(notificationService.getNotifications("EUR")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/notifications").param("currency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("EUR"));
    }

    @Test
    void getNotifications_returnsEmptyList() throws Exception {
        when(notificationService.getNotifications(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getNotifications_returnsNotFoundWhenCurrencyNotFound() throws Exception {
        when(notificationService.getNotifications("XYZ"))
                .thenThrow(new CurrencyCodeNotFoundException("XYZ"));

        mockMvc.perform(get("/api/v1/notifications").param("currency", "XYZ"))
                .andExpect(status().isNotFound());
    }
}
