package com.aggregator.notify.config;

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

    public static final String ALERT_EXCHANGE = "alert.exchange";
    public static final String ALERT_TRIGGERED_QUEUE = "alert.triggered";

    @Bean
    public DirectExchange alertExchange() {
        return new DirectExchange(ALERT_EXCHANGE);
    }

    @Bean
    public Queue alertTriggeredQueue() {
        return new Queue(ALERT_TRIGGERED_QUEUE, true);
    }

    @Bean
    public Binding alertTriggeredBinding(Queue alertTriggeredQueue, DirectExchange alertExchange) {
        return BindingBuilder.bind(alertTriggeredQueue)
                .to(alertExchange)
                .with(ALERT_TRIGGERED_QUEUE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
