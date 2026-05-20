package se.iths.erikthorell.orderservice.messaging;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationMessage(
        Long orderId,
        String customerEmail,
        List<OrderConfirmationItem> items,
        BigDecimal totalPrice
) {
    public record OrderConfirmationItem(
            String name,
            BigDecimal price,
            int quantity,
            BigDecimal lineTotal
    ) {
    }
}