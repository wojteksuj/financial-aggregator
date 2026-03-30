package com.aggregator.market;

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

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class RateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @InjectMocks
    private RateService rateService;

    @Test
    void testGetRatesInvalidDate(){
        String currencyCode = "USD";
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(InvalidRequestException.class, () -> rateService.getRates(currencyCode, date));
    }
}
