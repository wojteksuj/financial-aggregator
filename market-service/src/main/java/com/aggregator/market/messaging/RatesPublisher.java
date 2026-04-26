package com.aggregator.market.messaging;

import com.aggregator.market.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class RatesPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RatesPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRatesUpdate(Map<String, BigDecimal> currencyRates) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.MARKET_EXCHANGE,
                RabbitConfig.RATES_UPDATED_QUEUE,
                currencyRates
        );
    }
}
