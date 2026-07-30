package ru.yandex.practicum.analyzer.kafka;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import telemetry.BaseAvroDeserializer;

public class HubEventDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {

    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}