package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceAddedEventMapper implements HubEventMapper<DeviceAddedEvent> {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto event) {
        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setId(event.getDeviceAdded().getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceAdded().getType().name()))
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
