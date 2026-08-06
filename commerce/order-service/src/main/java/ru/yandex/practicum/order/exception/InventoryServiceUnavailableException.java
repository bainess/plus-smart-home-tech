package ru.yandex.practicum.order.exception;

public class InventoryServiceUnavailableException extends RuntimeException {
  public InventoryServiceUnavailableException(String message) {
    super(message);
  }

  public InventoryServiceUnavailableException(Long aLong, Throwable cause) {
  }
}
