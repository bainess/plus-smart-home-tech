package ru.yandex.practicum.analyzer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.mapper.ScenarioMapper;
import ru.yandex.practicum.analyzer.mapper.SensorMapper;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.ScenarioActionId;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubEventService {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioMapper scenarioMapper;

    public Sensor addSensor(HubEventAvro hubEvent) {
        Sensor sensor = SensorMapper.mapToSensor(hubEvent);
        sensor = sensorRepository.save(sensor);
        log.info("Sensor Event saved {}", sensor);
        return sensor;
    }

    public void removeSensor(HubEventAvro hubEvent) {
        Sensor sensor = SensorMapper.mapToSensor(hubEvent);
        sensorRepository
                .findByIdAndHubId(sensor.getId(), sensor.getHubId())
                .ifPresent(sensorRepository::delete);
        log.debug("Sensor removed");
    }

    @Transactional
    public Scenario addScenario(HubEventAvro hubEvent) {
        Optional<Scenario> scenario = scenarioMapper.mapToScenarioOptional(hubEvent);

        if (scenario.isEmpty()) {
            throw new IllegalArgumentException("Scenario is faulty");
        }
        Scenario sc = scenarioRepository.save(scenario.get());

        sc.getActions().forEach(action -> {
            action.setId(new ScenarioActionId(
                    sc.getId(),
                    action.getSensor().getId()
            ));
        });

        log.info("Scenario saved {}", sc);
        log.info("Action saved {}", sc);
        return sc;
    }

    @Transactional
    public void removeScenario(HubEventAvro hubEvent) {
        Scenario scenario = scenarioMapper.mapToScenarioOptional(hubEvent).orElseThrow(IllegalArgumentException::new);
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresent(scenarioRepository::delete);
        log.debug("Scenario removed");
    }
}
