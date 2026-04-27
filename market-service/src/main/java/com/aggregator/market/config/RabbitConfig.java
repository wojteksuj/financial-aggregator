package com.aggregator.market.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MARKET_EXCHANGE = "market.exchange";
    public static final String RATES_UPDATED_QUEUE = "rates.updated";

    @Bean
    public DirectExchange marketExchange() {
        return new DirectExchange(MARKET_EXCHANGE);
    }

    @Bean
    public Queue ratesUpdatedQueue() {
        return new Queue(RATES_UPDATED_QUEUE, true);
    }

    @Bean
    public Binding ratesUpdatedBinding(Queue ratesUpdatedQueue, DirectExchange marketExchange) {
        return BindingBuilder.bind(ratesUpdatedQueue)
                .to(marketExchange)
                .with(RATES_UPDATED_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
