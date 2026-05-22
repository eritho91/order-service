package se.iths.erikthorell.orderservice.dto;

import java.math.BigDecimal;

//Svaret order-service får tillbaka från product-service
//Det riktiga produktnamnet och priset visas
public record ProductInfoResponse(
        Long productId,
        String name,
        BigDecimal price,
        int quantity
) {
}