package ru.practicum.telemetry.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.telemetry.collector.model.sensor.mapper.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventHandler {
    private final CollectorProducer producer;
    private final SensorEventMapperRegistry registry;

    public void handle(SensorEvent event) {
        SensorEventMapper mapper = registry.get(event.getType());
        if (mapper == null) {
            throw new IllegalStateException("No mapper for type " + event.getType());
        }
        SensorEventAvro avro = mapper.toAvro(event);

        log.info("INFO SENT TO KAFKA {}", avro);
        producer.sendSensorEvent(avro);
    }
}
