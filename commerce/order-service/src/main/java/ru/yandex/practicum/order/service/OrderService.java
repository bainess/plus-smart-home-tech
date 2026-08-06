package ru.yandex.practicum.order.service;

import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.feign.model.Pending_Reason;

import java.util.List;


public interface OrderService {
    OrderDto createConfirmedOrder(CreateOrderRequest request);

    OrderDto createPendingOrder(CreateOrderRequest request,  Pending_Reason degraded_reason);

    OrderDto getOrder(Long id);

    List<OrderDto> getOrders();

    List<OrderDto> getOrdersByCustomerEmail(String customerEmail);

}
