package ru.yandex.practicum.order.feign.model;

public record ReserveRequest(
        Long productId,
        Integer quantity

) {
}