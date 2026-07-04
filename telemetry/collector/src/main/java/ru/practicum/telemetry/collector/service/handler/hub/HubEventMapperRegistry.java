package ru.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.HubEvent;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.hub.mapper.HubEventMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HubEventMapperRegistry {
    private final Map<HubEventType, HubEventMapper<? extends HubEvent>> mappers;

    public HubEventMapperRegistry(List<HubEventMapper<? extends HubEvent>> mapperList) {
        this.mappers = mapperList.stream().collect(Collectors.toMap(HubEventMapper::getType, Function.identity()));
    }

    public HubEventMapper<? extends HubEvent> get(HubEventType type) {
        return mappers.get(type);
    }
}
