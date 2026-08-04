package ru.yandex.practicum.inventory.exception;

public class AlreadtExistsException extends RuntimeException {

    public AlreadtExistsException(String message) {
        super(message);
    }
}
