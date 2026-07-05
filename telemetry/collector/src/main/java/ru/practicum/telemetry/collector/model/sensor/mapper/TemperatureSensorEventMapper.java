package ru.practicum.telemetry.collector.model.sensor.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Component
public class TemperatureSensorEventMapper implements SensorEventMapper<TemperatureSensorEvent> {
    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {
        TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureSensor().getTemperatureC())
                .setTemperatureF(event.getTemperatureSensor().getTemperatureF())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
