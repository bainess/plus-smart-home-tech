package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceActionMapper implements HubEventMapper<DeviceAction> {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ACTION;
    }

    @Override
    public HubEventAvro toAvro(DeviceAction event) {
        DeviceActionAvro payload = DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(event.getActionType().name()))
                .setSensorId(event.getSensorId())
                .setValue(event.getValue())
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
