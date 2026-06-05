package se.iths.erikthorell.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.iths.erikthorell.orderservice.dto.CreateOrderItemRequest;
import se.iths.erikthorell.orderservice.dto.OrderItemResponse;
import se.iths.erikthorell.orderservice.dto.OrderResponse;
import se.iths.erikthorell.orderservice.dto.ProductStockRequest;
import se.iths.erikthorell.orderservice.entity.CustomerOrder;
import se.iths.erikthorell.orderservice.entity.OrderItem;
import se.iths.erikthorell.orderservice.messaging.OrderConfirmationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", source = "productId")
    ProductStockRequest toProductStockRequest(CreateOrderItemRequest item);

    List<ProductStockRequest> toProductStockRequests(List<CreateOrderItemRequest> items);

    OrderItemResponse toOrderItemResponse(OrderItem item);

    OrderResponse toResponse(CustomerOrder order);

    OrderConfirmationDto.OrderItemDto toOrderConfirmationItemDto(OrderItem item);

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "lineTotal", source = "totalPrice")
    OrderConfirmationDto toOrderConfirmationDto(CustomerOrder order);
}