package ru.yandex.practicum.analyzer.kafka;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import telemetry.BaseAvroDeserializer;

public class SnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {

    public SnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
