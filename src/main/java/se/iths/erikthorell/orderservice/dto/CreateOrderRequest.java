package se.iths.erikthorell.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

//Det kunden skickar in
public record CreateOrderRequest(
        @NotEmpty(message = "Ordern måste innehålla minst en produkt")
        List<@Valid CreateOrderItemRequest> items
) {
}