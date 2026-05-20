package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

//Varje orderrad i svaret
public record OrderItemResponse(
        Long productId,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal lineTotal
) {
}