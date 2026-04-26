package com.aggregator.market.messaging;

import com.aggregator.market.config.RabbitConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RatesPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RatesPublisher ratesPublisher;

    @Test
    void publishRatesUpdate_sendsToCorrectExchangeAndQueue() {
        Map<String, BigDecimal> rates = Map.of("EUR", new BigDecimal("0.25"), "USD", new BigDecimal("0.22"));

        ratesPublisher.publishRatesUpdate(rates);

        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.MARKET_EXCHANGE,
                RabbitConfig.RATES_UPDATED_QUEUE,
                rates
        );
    }

    @Test
    void publishRatesUpdate_handlesEmptyMap() {
        Map<String, BigDecimal> emptyRates = Map.of();

        ratesPublisher.publishRatesUpdate(emptyRates);

        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.MARKET_EXCHANGE,
                RabbitConfig.RATES_UPDATED_QUEUE,
                emptyRates
        );
    }
}
