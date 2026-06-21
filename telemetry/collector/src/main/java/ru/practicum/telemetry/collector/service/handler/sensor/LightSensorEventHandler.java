package ru.practicum.telemetry.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.telemetry.collector.model.sensor.dto.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
public class LightSensorEventHandler implements SensorEventHandler {

    private final CollectorProducer producer;
    private final SensorEventMapper mapper;

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        SensorEventAvro avro = mapper.toAvro(event);
        producer.sendSensorEvent(avro);
    }
}
