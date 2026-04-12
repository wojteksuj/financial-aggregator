package com.aggregator.market.service;

import com.aggregator.market.dto.ExchangeRateApiResponse;
import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.exception.ExchangeRateApiException;
import com.aggregator.market.exception.InvalidRequestException;
import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class RateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateClient exchangeRateClient;
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
    }

    @Test
    void testGetRatesInvalidDate() {
        String currencyCode = "USD";
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(InvalidRequestException.class, () -> rateService.getRates(currencyCode, date));
    }

    @Test
    void testFetchAndSave() {
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "success",
                "PLN",
                new HashMap<>(Map.of("EUR", BigDecimal.valueOf(0.25)))));
        rateService.fetchAndSave();
        verify(exchangeRateRepository).saveAll(anyList());
    }

    void testFetchAndSaveExternalApiError(){
        when(exchangeRateClient.fetchRates()).thenReturn(new ExchangeRateApiResponse(
                "not_success",
                "PLN",
                new HashMap<>(Map.of("EUR", BigDecimal.valueOf(0.25)))));
        Assertions.assertThrows(ExchangeRateApiException.class, () -> rateService.fetchAndSave());
    }

}
