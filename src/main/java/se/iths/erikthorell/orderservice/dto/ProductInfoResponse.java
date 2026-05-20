package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

//Det order-service får tillbaka från product-service
public record ProductInfoResponse(
        Long productId,
        String name,
        BigDecimal price,
        int quantity
) {
}