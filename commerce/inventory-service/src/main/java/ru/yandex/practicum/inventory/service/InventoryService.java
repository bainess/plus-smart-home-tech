package ru.yandex.practicum.inventory.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getInventoryList();

    InventoryDto getInventory(Long productId);

    InventoryDto createInventory(UpdateInventoryRequest request);

    InventoryDto updateInventory(UpdateInventoryRequest request);

    ReserveResponse reserveProduct(ReserveRequest request);

    void releaseReserve(ReserveResponse request);
}
