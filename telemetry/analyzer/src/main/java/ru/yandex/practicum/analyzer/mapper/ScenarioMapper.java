package ru.yandex.practicum.analyzer.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScenarioMapper {
    private final SensorRepository sensorRepository;

    public Optional<Scenario> mapToScenarioOptional(HubEventAvro event) {
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
               Sensor sensor = sensorRepository.findByIdAndHubId(conditionAvro.getSensorId(), scenario.getHubId())
                        .orElseThrow(() -> new IllegalArgumentException("Sensor not found" + conditionAvro.getSensorId()));

                Condition condition = Condition.builder()
                        .type(ConditionTypeAvro.TEMPERATURE.valueOf(conditionAvro.getType().name()))
                        .operation(ConditionOperationAvro.valueOf(conditionAvro.getOperation().name()))
                        .value(ScenarioMapper.mapValue(conditionAvro.getValue()))
                        .build();

                ScenarioCondition scenarioCondition = ScenarioCondition.builder()
                        .id(new ScenarioConditionId())
                        .scenario(scenario)
                        .sensor(sensor)
                        .condition(condition).build();

                scenario.getConditions().add(scenarioCondition);
            });


            for (DeviceActionAvro actionAvro : ((ScenarioAddedEventAvro) payload).getActions()) {
                Action action = Action.builder()
                        .type(ActionTypeAvro.valueOf(actionAvro.getType().name()))
                        .value(ScenarioMapper.mapValue(actionAvro.getValue()))
                        .build();

                Sensor sensor = sensorRepository.findByIdAndHubId(actionAvro.getSensorId(), scenario.getHubId())
                        .orElseThrow(() -> new IllegalArgumentException("Sensor not found" + actionAvro.getSensorId()));


                ScenarioAction scenarioAction = ScenarioAction.builder()
                        .id(new ScenarioActionId(null, sensor.getId()))
                        .scenario(scenario)
                          .sensor(sensor)
                        .action(action)
                        .build();

                scenario.getActions().add(scenarioAction);
            }
            return Optional.of(scenario);

        }

        if (payload instanceof ScenarioRemovedEventAvro scenarioRemoved) {
            scenario = Scenario.builder()
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
