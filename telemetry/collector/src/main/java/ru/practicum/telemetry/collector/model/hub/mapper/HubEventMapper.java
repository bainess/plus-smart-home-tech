package ru.practicum.telemetry.collector.model.hub.mapper;

import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventMapper<T extends HubEvent> {

    HubEventType getType();
    HubEventAvro toAvro(T event);
}
