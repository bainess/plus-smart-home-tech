package ru.yandex.practicum.analyzer.mapper;

import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class SensorMapper {

    public static Sensor mapToSensor(HubEventAvro hubEvent) {
        Object payload = hubEvent.getPayload();
        String sensorId = null;
        if (payload instanceof DeviceAddedEventAvro deviceAdded) {
            sensorId = deviceAdded.getId();
        } else if (payload instanceof DeviceRemovedEventAvro deviceRemoved) {
            sensorId = deviceRemoved.getId();
        }

        Sensor sensor = Sensor.builder().hubId(hubEvent.getHubId()).id(sensorId).build();

        return sensor;
    }

}
