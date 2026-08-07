package ru.yandex.practicum.order.feign.model;

public record ReserveResponse(
        boolean success,
        Integer availableQuantity,
        Long productId,
        String message
) {
}