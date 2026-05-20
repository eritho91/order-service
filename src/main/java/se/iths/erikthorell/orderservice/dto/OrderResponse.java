package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//Svaret från order-service till frontend/Swagger
public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        String customerName,
        List<OrderItemResponse> orderItems,
        BigDecimal totalPrice
) {
}