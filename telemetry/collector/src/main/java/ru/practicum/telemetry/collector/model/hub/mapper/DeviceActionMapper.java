package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

@Component
public class DeviceActionMapper {

    public HubEventType getType() {
        return HubEventType.DEVICE_ACTION;
    }

    public DeviceActionAvro toAvro(DeviceAction event) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(event.getType().name()))
                .setSensorId(event.getSensorId())
                .setValue(event.getValue())
                .build();
    }
}
