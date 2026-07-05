package ru.practicum.telemetry.collector.service.handler.hub;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.hub.mapper.HubEventMapper;
import ru.practicum.telemetry.collector.model.hub.mapper.ScenarioAddedEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final CollectorProducer producer;
    private final ScenarioAddedEventMapper mapper;
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        HubEventAvro avro = mapper.toAvro(event);
        producer.sendHubEvent(avro);
    }
}
