package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal lineTotal
) {
}