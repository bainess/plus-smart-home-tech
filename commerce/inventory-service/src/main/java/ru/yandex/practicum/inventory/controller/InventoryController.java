package ru.yandex.practicum.inventory.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;
import ru.yandex.practicum.inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryDto> getInventoryList () {
        return inventoryService.getInventoryList();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryDto getInventory(@PathVariable("productId") Long productId) {
        return inventoryService.getInventory(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto postInventory(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.createInventory(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public InventoryDto updateInventory(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.updateInventory(request);
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    public ReserveResponse reserveProduct(@RequestBody ReserveRequest request) {
        return inventoryService.reserveProduct(request);
    }
}
