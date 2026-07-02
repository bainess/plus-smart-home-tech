package ru.practicum.telemetry.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.configuration.CollectorProducer;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.model.hub.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubEventHandler {
    private final CollectorProducer producer;
    private final HubEventMapperRegistry registry;

    public void handle(HubEvent event) {
        HubEventMapper mapper = registry.get(event.getType());
        if (mapper == null) {
            throw new IllegalStateException("No mapper for type " + event.getType());
        }

        HubEventAvro avro = mapper.toAvro(event);
        log.info("HUB info sent to KAFKA {}", avro);
        producer.sendHubEvent(avro);
    }
}
