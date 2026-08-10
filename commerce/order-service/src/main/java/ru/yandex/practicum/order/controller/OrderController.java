package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.feign.OrderOrchestrationService;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderOrchestrationService orchestrationService;
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequest request) {
        log.info("Программа входит здесь");
        return orchestrationService.createOrder(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        OrderDto order = orderService.getOrder(id);
        log.info("Showing order {}: {}", id, order);
        return order;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/by-email")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrdersByCustomerEmail(@RequestParam("email") String email) {
        List<OrderDto> orders = orderService.getOrdersByCustomerEmail(email);
        log.info("List of orders by email {}: {}", email, orders);
        return orders;
    }
}
