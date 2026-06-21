package ru.practicum.telemetry.collector.model.sensor.dto;

import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorEventMapper<T extends SensorEvent> {

    SensorEventAvro toAvro(T event);
}
