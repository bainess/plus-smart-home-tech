package ru.yandex.practicum.order.service;

import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.feign.model.Pending_Reason;
import ru.yandex.practicum.order.feign.model.ProductDto;

import java.util.List;
import java.util.Map;


public interface OrderService {
    OrderDto createConfirmedOrder(CreateOrderRequest request, Map<Long, ProductDto> product);

    OrderDto createPendingOrder(CreateOrderRequest request,  Map<Long, ProductDto> products, Pending_Reason degraded_reason);

    OrderDto getOrder(Long id);

    List<OrderDto> getOrders();

    List<OrderDto> getOrdersByCustomerEmail(String customerEmail);

}
