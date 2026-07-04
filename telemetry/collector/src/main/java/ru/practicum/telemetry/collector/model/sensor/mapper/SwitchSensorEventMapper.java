package ru.practicum.telemetry.collector.model.sensor.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component
public class SwitchSensorEventMapper implements SensorEventMapper<SwitchSensorEvent> {
    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {

        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(event.getSwitchSensor().getState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds()))
                .setPayload(payload)
                .build();
    }
}
