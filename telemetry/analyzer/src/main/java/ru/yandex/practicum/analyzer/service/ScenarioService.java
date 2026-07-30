package ru.yandex.practicum.analyzer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.client.HubRouter;
import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;


import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioService {

    private final SnapshotService service;
    private final ScenarioRepository scenarioRepository;
    private final HubRouter hubRouter;

    @Transactional
    public void applyScenario(String hubId) {
        log.info("Scenario for {} should be applied", hubId);
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
        SensorsSnapshotAvro snapshot = service.getSnapshot(hubId);

        for (Scenario scenario : scenarios) {
            if (!checkScenario(scenario, snapshot)) {
                continue;
            }

            for (ScenarioAction actionSc : scenario.getActions()) {
                Action action = Action.builder()
                        .value(actionSc.getAction().getValue())
                        .type(actionSc.getAction().getType())
                        .build();

                log.info(
                        "Sending action: hubId={}, scenario={}, sensorId={}, type={}, value={}",
                        hubId,
                        scenario.getName(),
                        actionSc.getSensor().getId(),
                        action.getType(),
                        action.getValue()
                );

                hubRouter.handleDeviceAction(hubId,
                        scenario.getName(),
                        actionSc.getSensor().getId(),
                        action,
                        Instant.now());

                log.info("Action send {}", scenario);
            }
        }
    }

    // проверка на соответствие всем условиям сценария
    private boolean checkScenario(Scenario scenario, SensorsSnapshotAvro snapshot) {
        return scenario.getConditions().stream().allMatch(condition -> checkCondition(snapshot, condition));
    }

    // проверка каждого условия
    private boolean checkCondition(SensorsSnapshotAvro snapshot, ScenarioCondition condition) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(condition.getSensor().getId());
        if (sensorState == null) {
            return false;
        }

        ConditionTypeAvro conditionType = condition.getCondition().getType();
        Object sensorData = sensorState.getData();
        log.info("SensorData {}", sensorData);

        return switch (conditionType) {
            case CO2LEVEL -> {
                ClimateSensorAvro climate = (ClimateSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(climate.getCo2Level()), condition.getCondition());
            }
            case HUMIDITY -> {
                ClimateSensorAvro climate = (ClimateSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(climate.getHumidity()), condition.getCondition());
            }
            case LUMINOSITY -> {
                LightSensorAvro light = (LightSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(light.getLuminosity()), condition.getCondition());
            }
            case MOTION -> {
                MotionSensorAvro motion = (MotionSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(motion.getMotion()), condition.getCondition());
            }
            case SWITCH -> {
                SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(switchSensor.getState()), condition.getCondition());
            }
            case TEMPERATURE -> {

                ClimateSensorAvro temperature = (ClimateSensorAvro) sensorData;
                yield handleCondition(conditionType, String.valueOf(temperature.getTemperatureC()),
                        condition.getCondition());
            }
            default -> false;
        };
    }

    //обработка конкретного условия
    private boolean handleCondition(ConditionTypeAvro conditionType, String sensorValue, Condition condition) {
        if (condition.getOperation() == null || sensorValue == null) return false;

        String conditionValue = condition.getValue().toString();
        String operation = condition.getOperation().name();

        if (conditionType.equals(ConditionTypeAvro.SWITCH) || conditionType.equals(ConditionTypeAvro.MOTION)) {
            if (!"EQUALS".equals(operation)) {
                return false;
            }

            boolean targetValue = "true".equals(conditionValue) || "1".equals(conditionValue);
            boolean currentValue = "true".equals(sensorValue) || "1".equals(sensorValue);

            return targetValue == currentValue;
        }

        try {
            int currentVal = Integer.parseInt(sensorValue);
            int targetVal = Integer.parseInt(conditionValue);

            return switch (operation) {
                case "EQUALS" -> currentVal == targetVal;
                case "GREATER_THAN" -> currentVal > targetVal;
                case "LOWER_THAN" -> currentVal < targetVal;
                default -> false;

            };
        } catch (NumberFormatException e) {
            // log.error("Cannot parse value sensor {}, target {}", sensorValue, conditionValue);
            return false;
        }
    }
}
