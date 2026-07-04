package ru.practicum.telemetry.collector.model.sensor.mapper;

import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorEventMapper<T extends SensorEvent> {

    SensorEventType getType();
    SensorEventAvro toAvro(T event);

}
