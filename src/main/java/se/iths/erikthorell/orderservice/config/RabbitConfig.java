package se.iths.erikthorell.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange emailExchange(
            @Value("${app.rabbitmq.email-exchange}") String exchangeName
    ) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue emailQueue(
            @Value("${app.rabbitmq.email-queue}") String queueName
    ) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding emailBinding(
            Queue emailQueue,
            DirectExchange emailExchange,
            @Value("${app.rabbitmq.email-routing-key}") String routingKey
    ) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(routingKey);
    }
}