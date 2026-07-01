package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventMapper implements HubEventMapper<ScenarioRemovedEvent> {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public HubEventAvro toAvro(ScenarioRemovedEvent event) {
        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
