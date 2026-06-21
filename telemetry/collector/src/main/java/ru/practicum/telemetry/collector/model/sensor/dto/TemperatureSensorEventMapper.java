package ru.practicum.telemetry.collector.model.sensor.dto;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventMapper implements SensorEventMapper<TemperatureSensorEvent> {
    @Override
    public SensorEventAvro toAvro(TemperatureSensorEvent event) {
        TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
