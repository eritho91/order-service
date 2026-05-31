package se.iths.erikthorell.orderservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.email-exchange}")
    private String exchange;

    @Value("${app.rabbitmq.email-routing-key}")
    private String routingKey;

    public void sendOrderConfirmation(OrderConfirmationDto message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}