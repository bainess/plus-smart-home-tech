package ru.practicum.telemetry.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.practicum.telemetry.collector.model.sensor.mapper.LightSensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class LightSensorEventHandler implements SensorEventHandler {
    private final CollectorProducer producer;
    private final LightSensorEventMapper mapper;

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro avro = mapper.toAvro(event);
        producer.sendSensorEvent(avro);
    }
}
