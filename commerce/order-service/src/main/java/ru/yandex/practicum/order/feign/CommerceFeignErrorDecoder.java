package ru.yandex.practicum.order.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.order.exception.OrderProcessingException;

public class CommerceFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new OrderProcessingException("Service was not found");
        }

        if (response.status() == 409) {
            return new OrderProcessingException("Operation cannot be complete");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
