package ru.practicum.telemetry.collector.model.sensor.dto;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class LightSensorEventMapper implements SensorEventMapper<LightSensorEvent> {

    @Override
    public SensorEventAvro toAvro(LightSensorEvent event) {
        LightSensorAvro payload = LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();

        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

}
