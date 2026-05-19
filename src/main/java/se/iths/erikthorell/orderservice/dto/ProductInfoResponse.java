package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

public record ProductInfoResponse(
        Long productId,
        String name,
        BigDecimal price,
        int quantity
) {
}