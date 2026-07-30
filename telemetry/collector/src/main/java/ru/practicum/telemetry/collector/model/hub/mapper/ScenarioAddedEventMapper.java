package ru.practicum.telemetry.collector.model.hub.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.practicum.telemetry.collector.model.hub.HubEventType;
import ru.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Component
public class ScenarioAddedEventMapper implements HubEventMapper<ScenarioAddedEvent> {
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto event) {
        List<ScenarioConditionAvro> conditions = event.getScenarioAdded().getConditionList().stream()
                .map(this::mapCondition)
                .toList();

        List<DeviceActionAvro> actions = event.getScenarioAdded().getActionList().stream()
                .map(this::mapAction)
                .toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getScenarioAdded().getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

    private DeviceActionAvro mapAction(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(
                        ActionTypeAvro.valueOf(action.getType().name())

                )
                .setValue(action.getValue())
                .build();
    }

    private ScenarioConditionAvro mapCondition(ScenarioConditionProto condition) {
        ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(
                        ConditionOperationAvro.valueOf(condition.getOperation().name())
                );

        switch (condition.getValueCase()) {
            case BOOL_VALUE -> builder.setValue(condition.getBoolValue());
            case INT_VALUE -> builder.setValue(condition.getIntValue());
            case VALUE_NOT_SET -> builder.setValue(null);
        }
        return builder.build();
    }
}
