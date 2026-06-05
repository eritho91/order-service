package se.iths.erikthorell.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

//Här skapas vägen orderbekräftelser tar till email-service
@Configuration
public class RabbitConfig {

    //Sorteringsterminal
    @Bean
    public DirectExchange emailExchange(
            @Value("${app.rabbitmq.email-exchange}") String exchangeName
    ) {
        return new DirectExchange(exchangeName);
    }

    //Här skapas kön där meddelandet hamnar
    @Bean
    public Queue emailQueue(
            @Value("${app.rabbitmq.email-queue}") String queueName
    ) {
        return new Queue(queueName, true);
    }

    //Här kopplas sorteringen ihop med kön
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

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}