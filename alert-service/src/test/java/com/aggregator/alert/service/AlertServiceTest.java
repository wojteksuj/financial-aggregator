package com.aggregator.alert.service;

import com.aggregator.alert.dto.request.CreateAlertRequest;
import com.aggregator.alert.dto.response.CreateAlertResponse;
import com.aggregator.alert.dto.response.GetAlertResponse;
import com.aggregator.alert.entity.Alert;
import com.aggregator.alert.exception.CurrencyCodeNotFoundException;
import com.aggregator.alert.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertService alertService;

    private Alert higherAlert;
    private Alert lowerAlert;

    @BeforeEach
    void setUp() {
        higherAlert = new Alert("USD", new BigDecimal("1.20"), true);
        lowerAlert = new Alert("USD", new BigDecimal("1.00"), false);
    }


    @Test
    void createAlert_savesAndReturnsResponse() {
        CreateAlertRequest request = new CreateAlertRequest("USD", new BigDecimal("1.20"), true);
        when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateAlertResponse response = alertService.createAlert(request);

        assertThat(response.currencyCode()).isEqualTo("USD");
        assertThat(response.thresholdRate()).isEqualByComparingTo("1.20");
        assertThat(response.higher()).isTrue();
        assertThat(response.active()).isTrue();
        verify(alertRepository).save(any(Alert.class));
    }


    @Test
    void getAlertsByCurrencyCode_returnsAlerts() {
        when(alertRepository.findAlertByCurrencyCode("USD")).thenReturn(List.of(higherAlert, lowerAlert));

        List<GetAlertResponse> result = alertService.getAlertsByCurrencyCode("USD");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.currencyCode().equals("USD"));
    }

    @Test
    void getAlertsByCurrencyCode_throwsWhenNoneFound() {
        when(alertRepository.findAlertByCurrencyCode("XYZ")).thenReturn(List.of());

        assertThatThrownBy(() -> alertService.getAlertsByCurrencyCode("XYZ"))
                .isInstanceOf(CurrencyCodeNotFoundException.class)
                .hasMessageContaining("XYZ");
    }


    @Test
    void checkAlerts_triggersHigherAlertWhenRateAboveThreshold() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of(higherAlert));

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("1.50"));

        assertThat(triggered).containsExactly(higherAlert);
        assertThat(higherAlert.isActive()).isFalse();
        verify(alertRepository).saveAll(List.of(higherAlert));
    }

    @Test
    void checkAlerts_doesNotTriggerHigherAlertWhenRateBelowThreshold() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of(higherAlert));

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("1.10"));

        assertThat(triggered).isEmpty();
        verify(alertRepository, never()).saveAll(any());
    }

    @Test
    void checkAlerts_triggersLowerAlertWhenRateBelowThreshold() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of(lowerAlert));

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("0.90"));

        assertThat(triggered).containsExactly(lowerAlert);
        assertThat(lowerAlert.isActive()).isFalse();
        verify(alertRepository).saveAll(List.of(lowerAlert));
    }

    @Test
    void checkAlerts_doesNotTriggerLowerAlertWhenRateAboveThreshold() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of(lowerAlert));

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("1.10"));

        assertThat(triggered).isEmpty();
        verify(alertRepository, never()).saveAll(any());
    }

    @Test
    void checkAlerts_doesNotTriggerWhenRateEqualsThreshold() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of(higherAlert, lowerAlert));

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("1.20"));

        assertThat(triggered).isEmpty();
        verify(alertRepository, never()).saveAll(any());
    }

    @Test
    void checkAlerts_returnsEmptyWhenNoActiveAlerts() {
        when(alertRepository.findAlertByCurrencyCodeAndActiveIsTrue("USD")).thenReturn(List.of());

        List<Alert> triggered = alertService.checkAlerts("USD", new BigDecimal("1.50"));

        assertThat(triggered).isEmpty();
        verify(alertRepository, never()).saveAll(any());
    }
}
