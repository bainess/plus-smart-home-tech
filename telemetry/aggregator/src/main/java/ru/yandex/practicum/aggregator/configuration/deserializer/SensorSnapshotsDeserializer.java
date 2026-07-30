package ru.yandex.practicum.aggregator.configuration.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorSnapshotsDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SensorSnapshotsDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
