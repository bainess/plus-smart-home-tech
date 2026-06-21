package ru.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.model.hub.HubEventType;

@Component(value = "DEVICE_ADDED")
public abstract class BaseHubEventHandler<T> {
    public abstract HubEventType getMessageType();

    protected abstract DeviceAddedEventAvro mapToAvro(HubEvent event);

}
