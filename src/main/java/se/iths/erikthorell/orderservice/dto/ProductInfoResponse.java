package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

public record ProductInfoResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock
) {
}