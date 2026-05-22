package se.iths.erikthorell.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.iths.erikthorell.orderservice.client.ProductClient;
import se.iths.erikthorell.orderservice.dto.*;
import se.iths.erikthorell.orderservice.entity.CustomerOrder;
import se.iths.erikthorell.orderservice.entity.OrderItem;
import se.iths.erikthorell.orderservice.messaging.OrderConfirmationMessage;
import se.iths.erikthorell.orderservice.messaging.OrderMessagePublisher;
import se.iths.erikthorell.orderservice.repository.CustomerOrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final ProductClient productClient;
    private final OrderMessagePublisher orderMessagePublisher;

    public OrderResponse createOrder(
            CreateOrderRequest request,
            String customerName,
            String bearerToken
    ) {
        log.info("Skapar order för kund: {}", customerName);

        List<ProductStockRequest> stockRequests = request.items().stream()
                .map(item -> new ProductStockRequest(
                        item.productId(),
                        item.quantity()
                ))
                .toList();

        log.info("Anropar product-service för att minska stock");

        List<ProductInfoResponse> products = productClient.decreaseStock(
                stockRequests,
                bearerToken
        );

        log.info("Svar mottaget från product-service: {} produkter", products.size());

        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(customerName);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (int i = 0; i < products.size(); i++) {
            ProductInfoResponse product = products.get(i);
            int quantity = request.items().get(i).quantity();

            BigDecimal lineTotal = product.price()
                    .multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.id());
            orderItem.setName(product.name());
            orderItem.setPrice(product.price());
            orderItem.setQuantity(quantity);
            orderItem.setLineTotal(lineTotal);

            order.addOrderItem(orderItem);
            totalPrice = totalPrice.add(lineTotal);
        }

        order.setTotalPrice(totalPrice);

        log.info("Sparar order i databasen");

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        log.info("Order sparad med id: {}", savedOrder.getId());

        OrderConfirmationMessage message = toOrderConfirmationMessage(savedOrder);

        log.info("Skickar orderbekräftelse till RabbitMQ");

        orderMessagePublisher.sendOrderConfirmation(message);

        log.info("Orderbekräftelse skickad");

        return toResponse(savedOrder);
    }

    private OrderResponse toResponse(CustomerOrder order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getCustomerName(),
                itemResponses,
                order.getTotalPrice()
        );
    }

    private OrderConfirmationMessage toOrderConfirmationMessage(CustomerOrder order) {
        List<OrderConfirmationMessage.OrderConfirmationItem> items = order.getOrderItems().stream()
                .map(item -> new OrderConfirmationMessage.OrderConfirmationItem(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderConfirmationMessage(
                order.getId(),
                order.getCustomerName(),
                items,
                order.getTotalPrice()
        );
    }
}