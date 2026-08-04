package ru.yandex.practicum.order.feign;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrchestrationService {
    private final OrderService orderService;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    public OrderDto createOrder(CreateOrderRequest request) {
        List<ReserveResponse> reservedProducts = new ArrayList<>();

        Map<Long, OrderItemRequest> productsRequest = request.items().stream()
                .collect(
                        Collectors.toMap(OrderItemRequest::productId,
                                Function.identity(),
                                (a, b) ->
                                        new OrderItemRequest(a.productId(),
                                                a.productName(),
                                                a.quantity() + b.quantity(),
                                                a.price())));

        for (OrderItemRequest item : productsRequest.values()) {
            try {
                ProductDto product = productClient.getProductById(item.productId());

                if (!product.active()) {
                    throw new OrderProcessingException("Product cannot be purchased");
                }
                    try {
                        ReserveRequest reserveRequest = new ReserveRequest(item.productId(), item.quantity());
                        ReserveResponse reserve = inventoryClient.reserveStock(reserveRequest);
                        reservedProducts.add(reserve);
                    }  catch (FeignException e) {
                reservedProducts.forEach(inventoryClient::releaseStock);
                    throw mapInventoryException(e, item.productId());
            }
            } catch (FeignException e) {
                throw mapProductException(e, item.productId());
            }
        }
        return orderService.createOrder(request);
    }

    private OrderProcessingException mapProductException(FeignException exception,
                                                         Long productId) {
        if (exception.status() == 404) {
            return new OrderProcessingException(
                    "Товар с id=%d не найден".formatted(productId)
            );
        }

        return new OrderProcessingException(
                "Не удалось получить данные товара"
        );
    }

    private OrderProcessingException mapInventoryException(FeignException exception,
                                                           Long productId) {
        if (exception.status() == 404) {
            return new OrderProcessingException(
                    "Складская запись для товара id=%d не найдена".formatted(productId)
            );
        }

        if (exception.status() == 409) {
            return new OrderProcessingException(
                    "Недостаточно товара id=%d на складе".formatted(productId)
            );
        }

        return new OrderProcessingException(
                "Не удалось зарезервировать товар"
        );
    }
}
