package se.iths.erikthorell.orderservice.dto;

public record ProductStockRequest(
        Long id,
        int quantity
) {
}