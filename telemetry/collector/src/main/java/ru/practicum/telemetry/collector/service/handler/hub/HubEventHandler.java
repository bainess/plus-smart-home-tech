package ru.practicum.telemetry.collector.service.handler.hub;

import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.sensor.SensorEvent;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handle(SensorEvent event);
}
