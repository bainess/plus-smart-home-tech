package ru.practicum.telemetry.collector.model.sensor.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
public class MotionSensorEventMapper implements SensorEventMapper<MotionSensorEvent>{

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto event) {

        MotionSensorAvro payload = MotionSensorAvro.newBuilder()
               .setLinkQuality(event.getMotionSensor().getLinkQuality())
                .setMotion(event.getMotionSensor().getMotion())
                .setVoltage(event.getMotionSensor().getVoltage())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds()))
                .setPayload(payload)
                .build();
    }
}
