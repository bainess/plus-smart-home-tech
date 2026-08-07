package ru.yandex.practicum.order.exception;

public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException(String message) {
        super(message);
    }

    public ProductServiceUnavailableException(Long productId, Throwable cause) {
    }
}
