package se.iths.erikthorell.orderservice.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderConfirmationDto(
        String customerName,
        Long id,
        List<OrderItemDto> items,
        BigDecimal lineTotal,
        BigDecimal totalPrice,
        LocalDateTime orderDate
) {
    public record OrderItemDto(
            Long productId,
            String name,
            BigDecimal price,
            int quantity,
            BigDecimal lineTotal
    ) {
    }
}