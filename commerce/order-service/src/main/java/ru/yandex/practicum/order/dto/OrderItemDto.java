package ru.yandex.practicum.order.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDto(

        Long id,

        Long productId,

        String productName,

        Integer quantity,

        BigDecimal price
) {
}