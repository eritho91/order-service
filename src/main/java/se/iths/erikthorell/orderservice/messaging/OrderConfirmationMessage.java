package se.iths.erikthorell.orderservice.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderConfirmationMessage(
        String customerEmail,
        Long id,
        List<OrderConfirmationItem> items,
        BigDecimal totalPrice,
        LocalDateTime orderDate
) {
    public record OrderConfirmationItem(
            String productName,
            int quantity,
            BigDecimal price
    ) {
    }
}