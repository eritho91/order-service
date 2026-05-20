package se.iths.erikthorell.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import se.iths.erikthorell.orderservice.dto.CreateOrderRequest;
import se.iths.erikthorell.orderservice.dto.OrderResponse;
import se.iths.erikthorell.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String customerName = jwt.getSubject();
        String bearerToken = "Bearer " + jwt.getTokenValue();

        return orderService.createOrder(request, customerName, bearerToken);
    }
}