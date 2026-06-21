package ru.practicum.telemetry.collector.model.sensor.dto;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventMapper implements SensorEventMapper<SwitchSensorEvent> {
    @Override
    public SensorEventAvro toAvro(SwitchSensorEvent event) {
        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(event.isState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
