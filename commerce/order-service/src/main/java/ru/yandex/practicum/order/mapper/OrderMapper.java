package ru.yandex.practicum.order.mapper;

import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order) {
        List<OrderItemDto> orderItemsList = order.getItems().stream().map(OrderMapper::mapToOrderItemDto).toList();

        OrderDto dto = new OrderDto(order.getId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getStatusDetails(),
                order.getCreatedAt(),
                orderItemsList);
        return dto;
    }

    private static OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice()

        );
        return  dto;
    }

    public static Order mapToOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .customerName(request.customerName())
                .customerEmail(request.customerEmail())
                .createdAt(LocalDateTime.now())
                .status("CREATED")
                .items(new ArrayList<>())
                .build();

        for (OrderItemRequest orderItemRequest : request.items()) {
            OrderItem orderItem = OrderMapper.mapToOrderItem(orderItemRequest);
            order.getItems().add(orderItem);
        }
        return order;
    }

    private static OrderItem mapToOrderItem(OrderItemRequest request) {
        OrderItem orderItem = OrderItem.builder()
                .productId(request.productId())
                .productName(request.productName())
                .quantity(request.quantity())
                .price(request.price()).build();
        return orderItem;
    }
}
