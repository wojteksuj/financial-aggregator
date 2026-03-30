package com.aggregator.market;

import com.aggregator.market.repository.ExchangeRateRepository;
import com.aggregator.market.service.RateService;
import com.aggregator.market.service.component.ExchangeRateClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RateServiceTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @InjectMocks
    private RateService rateService;
}
