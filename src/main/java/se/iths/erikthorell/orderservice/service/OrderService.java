package se.iths.erikthorell.orderservice.service;

import lombok.RequiredArgsConstructor;
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
        List<ProductStockRequest> stockRequests = request.items().stream()
                .map(item -> new ProductStockRequest(
                        item.productId(),
                        item.quantity()
                ))
                .toList();

        List<ProductInfoResponse> products = productClient.decreaseStock(
                stockRequests,
                bearerToken
        );

        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(customerName);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (ProductInfoResponse product : products) {
            BigDecimal lineTotal = product.price()
                    .multiply(BigDecimal.valueOf(product.quantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.productId());
            orderItem.setName(product.name());
            orderItem.setPrice(product.price());
            orderItem.setQuantity(product.quantity());
            orderItem.setLineTotal(lineTotal);

            order.addOrderItem(orderItem);

            totalPrice = totalPrice.add(lineTotal);
        }

        order.setTotalPrice(totalPrice);

        CustomerOrder savedOrder = customerOrderRepository.save(order);

        OrderConfirmationMessage message = toOrderConfirmationMessage(savedOrder);
        orderMessagePublisher.sendOrderConfirmation(message);

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