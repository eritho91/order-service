package se.iths.erikthorell.orderservice.dto;

//Det order-service skickar till product-service
public record ProductStockRequest(
        Long productId,
        int quantity
) {
}