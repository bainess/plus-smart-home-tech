package ru.yandex.practicum.order.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.order.fallback.InventoryClientFallbackFactory;
import ru.yandex.practicum.order.feign.model.ReserveRequest;
import ru.yandex.practicum.order.feign.model.ReserveResponse;

@FeignClient(name = "inventory-service",
fallbackFactory = InventoryClientFallbackFactory.class)
public interface InventoryClient {

    @PostMapping("/api/inventory/reserve")
    ReserveResponse reserveStock(@RequestBody ReserveRequest request);

    @PostMapping("api/inventory/reserve")
    void releaseStock(@RequestBody ReserveResponse response);
}