package ru.practicum.telemetry.collector.service.handler.sensor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.sensor.mapper.SensorEventMapper;
import ru.practicum.telemetry.collector.model.sensor.mapper.TemperatureSensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@AllArgsConstructor
public class TemperatureSensorEventHandler implements SensorEventHandler {

    private final CollectorProducer producer;
    private final TemperatureSensorEventMapper mapper;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro avro = mapper.toAvro(event);

        producer.sendSensorEvent(avro);
    }
}
