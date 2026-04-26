package com.aggregator.market.service;

import com.aggregator.market.dto.ExchangeRateApiResponse;
import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.exception.ExchangeRateApiException;
import com.aggregator.market.exception.InvalidRequestException;
import com.aggregator.market.exception.RatesNotFoundException;
import com.aggregator.market.messaging.RatesPublisher;
import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @Mock
    private RatesPublisher ratesPublisher;
    @InjectMocks
    private RateService rateService;

    @Test
    void testGetRates() {
        when(exchangeRateRepository.findByCurrencyCodeAndFetchedAtBetween(eq("EUR"), any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(new ExchangeRate("EUR", "PLN",
                        new BigDecimal("4.25"), Instant.now())));

        List<RateResponseDto> result = rateService.getRates("EUR", LocalDate.now());

        assertEquals(1, result.size());
        assertEquals("EUR", result.get(0).currencyCode());
        assertEquals("PLN", result.get(0).baseCurrency());
        assertEquals(new BigDecimal("4.25"), result.get(0).rate());
    }

    @Test
    void testGetRatesReturnsEmptyListWhenNoneFound() {
        when(exchangeRateRepository.findByCurrencyCodeAndFetchedAtBetween(eq("USD"), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        List<RateResponseDto> result = rateService.getRates("USD", LocalDate.now());

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRatesInvalidDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(InvalidRequestException.class, () -> rateService.getRates("USD", futureDate));
    }

    @Test
    void testGetRatesTodayDateIsAllowed() {
        when(exchangeRateRepository.findByCurrencyCodeAndFetchedAtBetween(eq("EUR"), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> rateService.getRates("EUR", LocalDate.now()));
    }

    @Test
    void testGetRatesPastDateIsAllowed() {
        when(exchangeRateRepository.findByCurrencyCodeAndFetchedAtBetween(eq("EUR"), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> rateService.getRates("EUR", LocalDate.now().minusDays(7)));
    }

    @Test
    void testFetchAndSavePersistsRates() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "success",
                "PLN",
                new HashMap<>(Map.of("EUR", BigDecimal.valueOf(0.25)))));

        rateService.fetchAndSave();

        verify(exchangeRateRepository).saveAll(anyList());
    }

    @Test
    void testFetchAndSavePublishesRatesUpdate() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "success",
                "PLN",
                new HashMap<>(Map.of("EUR", new BigDecimal("0.25"), "USD", new BigDecimal("0.22")))));

        rateService.fetchAndSave();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, BigDecimal>> captor = ArgumentCaptor.forClass(Map.class);
        verify(ratesPublisher).publishRatesUpdate(captor.capture());
        Map<String, BigDecimal> published = captor.getValue();
        assertEquals(new BigDecimal("0.25"), published.get("EUR"));
        assertEquals(new BigDecimal("0.22"), published.get("USD"));
    }

    @Test
    void testFetchAndSaveFiltersUnsupportedCurrencies() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "success",
                "PLN",
                new HashMap<>(Map.of("EUR", BigDecimal.valueOf(0.25), "XYZ", BigDecimal.valueOf(1.0)))));

        rateService.fetchAndSave();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<ExchangeRate>> captor = ArgumentCaptor.forClass(List.class);
        verify(exchangeRateRepository).saveAll(captor.capture());
        List<ExchangeRate> saved = captor.getValue();
        assertTrue(saved.stream().allMatch(r -> !r.getCurrencyCode().equals("XYZ")));
        assertTrue(saved.stream().anyMatch(r -> r.getCurrencyCode().equals("EUR")));
    }

    @Test
    void testFetchAndSaveThrowsOnExternalApiError() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "error",
                "PLN",
                new HashMap<>(Map.of("EUR", BigDecimal.valueOf(0.25)))));

        assertThrows(ExchangeRateApiException.class, () -> rateService.fetchAndSave());
        verifyNoInteractions(exchangeRateRepository);
    }

    @Test
    void testFetchAndSaveThrowsWhenConversionRatesEmpty() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "success",
                "PLN",
                new HashMap<>()));

        assertThrows(RatesNotFoundException.class, () -> rateService.fetchAndSave());
        verifyNoInteractions(exchangeRateRepository);
    }
}
