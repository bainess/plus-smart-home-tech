package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedEventMapper implements HubEventMapper<ScenarioAddedEvent> {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro toAvro(ScenarioAddedEvent event) {
        List<ScenarioConditionAvro> conditions = event.getConditions().stream()
                .map(this::mapCondition)
                .toList();

        List<DeviceActionAvro> actions = event.getActions().stream()
                .map(this::mapAction)
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private DeviceActionAvro mapAction(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(
                        ActionTypeAvro.valueOf(
                                action.getActionType().name()
                        )
                )
                .setValue(action.getValue())
                .build();
    }

    private ScenarioConditionAvro mapCondition(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setValue(condition.getValue())
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .build();
    }
}
