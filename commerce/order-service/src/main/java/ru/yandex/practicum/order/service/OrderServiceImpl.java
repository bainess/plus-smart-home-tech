package ru.yandex.practicum.order.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        Order order = OrderMapper.mapToOrder(request);
        order.setTotalPrice(order.getItems().stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
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
        return orders.stream().map(OrderMapper::mapToOrderDto).toList();
    }
}
