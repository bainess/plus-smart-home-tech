    package ru.yandex.practicum.order.feign;

    import feign.FeignException;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import ru.yandex.practicum.order.dto.CreateOrderRequest;
    import ru.yandex.practicum.order.dto.OrderDto;
    import ru.yandex.practicum.order.dto.OrderItemRequest;
    import ru.yandex.practicum.order.exception.InventoryServiceUnavailableException;
    import ru.yandex.practicum.order.exception.OrderProcessingException;
    import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;
    import ru.yandex.practicum.order.fallback.RemoteCallResult;
    import ru.yandex.practicum.order.feign.client.InventoryClient;
    import ru.yandex.practicum.order.feign.client.ProductClient;
    import ru.yandex.practicum.order.feign.model.Pending_Reason;
    import ru.yandex.practicum.order.feign.model.ProductDto;
    import ru.yandex.practicum.order.feign.model.ReserveRequest;
    import ru.yandex.practicum.order.feign.model.ReserveResponse;
    import ru.yandex.practicum.order.service.OrderService;

    import java.math.BigDecimal;
    import java.util.ArrayList;
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
            List<ProductDto> productFailedToSend = new ArrayList<>();

            Map<Long, OrderItemRequest> productsRequest = request.items().stream()
                    .collect(
                            Collectors.toMap(OrderItemRequest::productId,
                                    Function.identity(),
                                    (a, b) ->
                                            new OrderItemRequest(a.productId(),
                                                    a.productName(),
                                                    a.quantity() + b.quantity(),
                                                    a.price())));

            boolean degraded = false;
            Pending_Reason degradedReason = null;

            try {
                for (OrderItemRequest item : productsRequest.values()) {

                    RemoteCallResult<ProductDto> productCallResult = getProduct(item.productId());
                    ProductDto product;

                    switch (productCallResult) {
                        case RemoteCallResult.Success<ProductDto> success -> {
                             product = success.value();
                            if (!product.active()) {

                                throw new OrderProcessingException("Product cannot be purchased");
                            }

                        }
                        case RemoteCallResult.BusinessFailure<ProductDto> failure -> {
                                throw new OrderProcessingException(failure.message());
                        }
                        case RemoteCallResult.TechnicalFailure<ProductDto> failedToSendProduct -> {
                            degraded = true;
                            degradedReason = Pending_Reason.PRODUCT_UNAVAILABLE;

                            String name = "Product id=%d (awaits check)".formatted(item.productId());
                            String status = "PENDING_CONFIRMATION";
                            product = new ProductDto(0L, name, status, BigDecimal.ZERO, false);

                            productFailedToSend.add(product);
                        }
                    }

                    RemoteCallResult<ReserveResponse> reserveResult = reserveProduct(item);

                    switch (reserveResult) {
                        case RemoteCallResult.Success<ReserveResponse> success -> {
                            reservedProducts.add(success.value());
                        }
                        case RemoteCallResult.BusinessFailure<ReserveResponse> failure -> {
                            reservedProducts.forEach(inventoryClient::releaseStock);
                            throw new OrderProcessingException(failure.message());
                        }
                        case RemoteCallResult.TechnicalFailure<ReserveResponse> failedToSend -> {
                            degraded = true;
                            degradedReason = Pending_Reason.INVENTORY_UNAVAILABLE;

                            String name = "Reservation id=%d (awaits check)".formatted(item.productId());
                            String status = "PENDING_CONFIRMATION";
                            productFailedToSend.add(new ProductDto(0L, name, status, BigDecimal.ZERO, false)
                            );

                        }
                    }
                }
            } catch (OrderProcessingException e) {
                reservedProducts.forEach(inventoryClient::releaseStock);
                throw e;
            }

            if (degraded) {
                return orderService.createPendingOrder(request, degradedReason);
            }
            return orderService.createConfirmedOrder(request);
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

    private RemoteCallResult<ProductDto> getProduct(Long productId) {
        try {
            return new RemoteCallResult.Success<>(productClient.getProductById(productId));
        } catch (FeignException.NotFound e) {
            return new RemoteCallResult.BusinessFailure<>("Product id=%d not found".formatted(productId));
        } catch (ProductServiceUnavailableException exception) {
            return new RemoteCallResult.TechnicalFailure<>("Catalog is temporarily unavailable");
        }
    }

    private RemoteCallResult<ReserveResponse> reserveProduct(OrderItemRequest item) {
        try {
            return new RemoteCallResult.Success<>(inventoryClient.reserveStock(
                    new ReserveRequest(item.productId(), item.quantity())));
        } catch (FeignException.NotFound e) {
            return new RemoteCallResult.BusinessFailure<>("Product id = %d not found".formatted(item.productId()));
        } catch (FeignException.Conflict e) {
            return new RemoteCallResult.BusinessFailure<>("Not enough product in stock");
        } catch (InventoryServiceUnavailableException e) {
            return new RemoteCallResult.TechnicalFailure<>("Inventory service unavailable");
        }
    }
    }
