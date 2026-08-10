package ru.yandex.practicum.order.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.entity.Status;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.feign.client.InventoryClient;
import ru.yandex.practicum.order.feign.client.ProductClient;
import ru.yandex.practicum.order.feign.model.Pending_Reason;
import ru.yandex.practicum.order.feign.model.ProductDto;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;


    @Override
    @Transactional
    public OrderDto createConfirmedOrder(CreateOrderRequest request, Map<Long, ProductDto> products) {
        log.info("Start order for request " + request);
        Order order = OrderMapper.mapToOrder(request, products);

        order.setTotalPrice(order.getItems().stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        log.info("Order mapped: " + order);
        order.setStatus(Status.CONFIRMED.toString());
        order = orderRepository.save(order);

        OrderDto o = OrderMapper.mapToOrderDto(order);
        log.info("Order saved: {}",o);
        return o;
    }

    @Override
    @Transactional
    public OrderDto createPendingOrder(CreateOrderRequest request, Map<Long,ProductDto> products, Pending_Reason degraded_reason) {

        Order order = OrderMapper.mapToOrder(request, products);

        order.setTotalPrice(order.getItems().stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setStatus(Status.PENDING_CONFIRMATION.toString());
        order.setStatusDetails(degraded_reason.toString());
        order = orderRepository.save(order);
        return OrderMapper.mapToOrderDto(order);
    }


    @Override
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Oder " + id + " not found"));
        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderMapper::mapToOrderDto).toList();
    }

    @Override
    public List<OrderDto> getOrdersByCustomerEmail(String customerEmail) {
        List<Order> orders = orderRepository.findAllByCustomerEmail(customerEmail);
        log.info("Orders from service by email {} orders: {}", customerEmail, orders);
        return orders.stream().map(OrderMapper::mapToOrderDto).toList();
    }
}
