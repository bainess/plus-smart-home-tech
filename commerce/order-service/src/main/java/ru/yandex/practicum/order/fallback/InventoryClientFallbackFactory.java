package ru.yandex.practicum.order.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.InventoryServiceUnavailableException;
import ru.yandex.practicum.order.feign.client.InventoryClient;
import ru.yandex.practicum.order.feign.model.ReserveRequest;
import ru.yandex.practicum.order.feign.model.ReserveResponse;

@Component
public class InventoryClientFallbackFactory implements FallbackFactory<InventoryClient> {
    private static final Logger log = LoggerFactory.getLogger(InventoryClientFallbackFactory.class);

    @Override
    public InventoryClient create(Throwable cause) {
        return new InventoryClient() {
            @Override
            public ReserveResponse reserveStock(ReserveRequest request) {
                log.warn("inventory-service is not available for product id ={} reservation", request.productId(),
                        cause);
                throw new InventoryServiceUnavailableException(request.productId(), cause);
            }

            @Override
            public void releaseStock(ReserveResponse response) {
                log.warn("inventory-service is not available to release reserve for product id={}", response.productId(),
                        cause);
                throw new InventoryServiceUnavailableException(response.productId(), cause);
            }
        };
    }
}
