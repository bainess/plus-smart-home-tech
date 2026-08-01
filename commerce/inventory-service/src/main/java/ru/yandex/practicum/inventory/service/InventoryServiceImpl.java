package ru.yandex.practicum.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;
import ru.yandex.practicum.inventory.exception.AlreadtExistsException;
import ru.yandex.practicum.inventory.exception.InsufficientStockException;
import ru.yandex.practicum.inventory.exception.NotFoundException;
import ru.yandex.practicum.inventory.mapper.InventoryMapper;
import ru.yandex.practicum.inventory.repository.InventoryRepository;

import java.rmi.AlreadyBoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;

    @Override
    public List<InventoryDto> getInventoryList() {
        return inventoryRepository.findAll().stream().map(InventoryMapper::mapToInventoryDto).toList();
    }

    @Override
    public InventoryDto getInventory(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("Product " + productId + " not found"));
        return InventoryMapper.mapToInventoryDto(inventory);
    }

    @Override
    public InventoryDto createInventory(UpdateInventoryRequest request) {
        if (inventoryRepository.findByProductId(request.productId()).isPresent()) {
            throw new AlreadtExistsException("Product " + request.productId() + " already exists");
        }
        Inventory inventory = InventoryMapper.mapToInventory(request);
        inventory = inventoryRepository.save(inventory);
        return InventoryMapper.mapToInventoryDto(inventory);
    }

    @Override
    public InventoryDto updateInventory(UpdateInventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException("Product " + request.productId() + " not found"));
        inventory.setQuantity(request.quantity());
        inventory = inventoryRepository.save(inventory);

        return InventoryMapper.mapToInventoryDto(inventory);
    }

    @Override
    public ReserveResponse reserveProduct(ReserveRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException("Product " + request.productId() + " not found"));
        log.info("Inventory before reservation {}, available {}", inventory, inventory.getAvailableQuantity());
        if (inventory.getAvailableQuantity() - request.quantity() >= 0) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + request.quantity());
            inventoryRepository.save(inventory);
        } else {
            throw  new InsufficientStockException("Product " + inventory.getProductId() + " is not enough to reserve");
        }
        log.info("Inventory after reservation {}, available {}", inventory, inventory.getAvailableQuantity());
        return new ReserveResponse(true, inventory.getAvailableQuantity(), "success");
    }
}
