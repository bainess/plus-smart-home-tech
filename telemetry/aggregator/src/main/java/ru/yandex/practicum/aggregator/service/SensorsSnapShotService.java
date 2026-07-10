package ru.yandex.practicum.aggregator.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SensorsSnapShotService {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();



    public Optional<SensorsSnapshotAvro> updateSensorSnapShot(SensorEventAvro event) {
        log.info("SensorEven received - {}", event);
        SensorsSnapshotAvro snapshotAvro;
        if (snapshots.get(event.getHubId()) != null) {
            snapshotAvro = snapshots.get(event.getHubId());

        } else {
            snapshotAvro = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(new HashMap<>())
                    .build();
        }
        Map<String,SensorStateAvro> states = snapshotAvro.getSensorsState();
        SensorStateAvro oldState = states.get(event.getId());

        if (oldState != null) {
            // если событие опоздало, ничего не возвращать
            if (oldState.getTimestamp().isAfter(event.getTimestamp())) {
                return Optional.empty();
            }
            // если объекты равны, ничего не возвращать
            if (Objects.deepEquals(oldState.getData(), event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        states.put(event.getId(), newState);
        snapshotAvro.setTimestamp(event.getTimestamp());

        snapshots.put(event.getHubId(), snapshotAvro);

        return Optional.of(snapshotAvro);
    }
}
