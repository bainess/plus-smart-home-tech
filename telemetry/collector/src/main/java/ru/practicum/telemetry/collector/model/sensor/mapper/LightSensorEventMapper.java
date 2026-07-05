package ru.practicum.telemetry.collector.model.sensor.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LightSensorEventMapper implements SensorEventMapper<LightSensorEvent> {

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {


        LightSensorAvro payload = LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLightSensor().getLinkQuality())
                .setLuminosity(event.getLightSensor().getLuminosity())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

}
