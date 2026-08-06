package ru.yandex.practicum.order.fallback;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;
import ru.yandex.practicum.order.feign.client.ProductClient;

@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {
    private static final Logger log = LoggerFactory.getLogger(ProductClientFallbackFactory.class);

    @Override
    public ProductClient create(Throwable cause) {
        return productId -> {
            log.warn("product-service is not available for product id ={}", productId, cause);
            throw new ProductServiceUnavailableException(productId, cause);
        };
    }
}
