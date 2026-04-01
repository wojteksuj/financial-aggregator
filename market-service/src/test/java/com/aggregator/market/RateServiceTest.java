package com.aggregator.market;

import com.aggregator.market.dto.RateResponseDto;
import com.aggregator.market.entity.ExchangeRate;
import com.aggregator.market.exception.InvalidRequestException;
import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.RateService;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
        when(exchangeRateRepository.findByCurrencyCodeAndFetchedAtBetween("EUR", any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(new ExchangeRate("EUR", "PLN",
                        new BigDecimal("4.25"), Instant.now())));

        List<RateResponseDto> result = rateService.getRates("EUR", LocalDate.now());

        assertEquals(1, result.size());
        assertEquals("EUR", result.get(0).baseCurrency());
    }

    @Test
    void testGetRatesInvalidDate() {
        String currencyCode = "USD";
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(InvalidRequestException.class, () -> rateService.getRates(currencyCode, date));
    }


}
