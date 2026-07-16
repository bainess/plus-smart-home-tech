package ru.yandex.practicum.analyzer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.mapper.ScenarioMapper;
import ru.yandex.practicum.analyzer.mapper.SensorMapper;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.ActionRepository;
import ru.yandex.practicum.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Optional;

@Service
@Slf4j
public class HubEventService {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
//    private final ConditionRepository conditionRepository;
//    private final ActionRepository actionRepository;
    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub hubRouterClient;

    public HubEventService(SensorRepository sensorRepository,
                           ScenarioRepository scenarioRepository) {
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public Sensor addSensor(HubEventAvro hubEvent) {
        Sensor sensor = SensorMapper.mapToSensor(hubEvent);
        return sensorRepository.save(sensor);
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
        Optional<Scenario> scenario = ScenarioMapper.mapToScenarioOptional(hubEvent);

        if (scenario.isEmpty()) {
            throw new IllegalArgumentException("Scenario is faulty");
        }
        return scenarioRepository.save(scenario.get());
    }

    @Transactional
    public void removeScenario(HubEventAvro hubEvent) {
        Scenario scenario = ScenarioMapper.mapToScenarioOptional(hubEvent).orElseThrow(IllegalArgumentException::new);
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresent(scenarioRepository::delete);
        log.debug("Scenario removed");
    }
}
