package ru.yandex.practicum.analyzer.mapper;

import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.Optional;

public class ScenarioMapper {
    public static Optional<Scenario> mapToScenarioOptional(HubEventAvro event) {
        Object payload = event.getPayload();
        Scenario scenario;

        if (payload instanceof ScenarioAddedEventAvro scenarioAdded) {
            scenario = Scenario.builder()
                    .name(scenarioAdded.getName())
                    .hubId(event.getHubId())
                    .conditions(new ArrayList<>())
                    .actions(new ArrayList<>())
                    .build();

            scenarioAdded.getConditions().forEach(conditionAvro -> {
                Sensor sensor = Sensor.builder()
                        .id(conditionAvro.getSensorId())
                        .hubId(event.getHubId())
                        .build();



                Condition condition = Condition.builder()
                        .type(ConditionType.valueOf(conditionAvro.getType().name()))
                        .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                        .value((Integer) conditionAvro.getValue())
                        .build();

                ScenarioCondition scenarioCondition = ScenarioCondition.builder()
                        .scenario(scenario)
                        .sensor(sensor)
                        .condition(condition).build();

                scenario.getConditions().add(scenarioCondition);
            });

            
            for (DeviceActionAvro actionAvro : ((ScenarioAddedEventAvro) payload).getActions()) {
                Action action = Action.builder()
                        .type(ActionType.valueOf(actionAvro.getType().name()))
                        .value(ScenarioMapper.mapValue(actionAvro.getValue()))
                        .build();

                Sensor sensor = Sensor.builder()
                        .id(actionAvro.getSensorId()).hubId(event.getHubId()).build();


                ScenarioAction scenarioAction = ScenarioAction.builder()
                        .scenario(scenario)
                        .sensor(sensor)
                        .action(action)
                        .build();

                scenario.getActions().add(scenarioAction);
            }
            return Optional.of(scenario);

        }

        if (payload instanceof ScenarioRemovedEventAvro scenarioRemoved) {
            scenario =  Scenario.builder()
                    .hubId(scenarioRemoved.getName())
                    .name(scenarioRemoved.getName())
                    .build();
            return Optional.of(scenario);
        }
        return Optional.empty();
    }

    private static Integer mapValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer i) {
            return i;
        }

        if (value instanceof Boolean b) {
            return b ? 1 : 0;
        }

        throw new IllegalArgumentException(
                "Unsupported value type: " + value.getClass()
        );
    }
}
