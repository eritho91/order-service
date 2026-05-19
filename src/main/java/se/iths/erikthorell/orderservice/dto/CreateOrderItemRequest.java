package se.iths.erikthorell.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull(message = "Produkt-id måste anges")
        Long productId,

        @Min(value = 1, message = "Antal måste vara minst 1")
        int quantity
) {
}