package se.iths.erikthorell.orderservice.dto;

public record ProductStockRequest(
        Long productId,
        int quantity
) {
}