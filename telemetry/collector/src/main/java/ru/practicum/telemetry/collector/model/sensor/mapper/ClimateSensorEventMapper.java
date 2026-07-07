package ru.practicum.telemetry.collector.model.sensor.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.ClimateSensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class ClimateSensorEventMapper implements SensorEventMapper<ClimateSensorEvent> {
    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {

        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getClimateSensor().getCo2Level())
                .setHumidity(event.getClimateSensor().getHumidity())
                .setTemperatureC(event.getClimateSensor().getTemperatureC())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }
}
