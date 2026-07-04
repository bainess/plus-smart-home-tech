//package ru.practicum.telemetry.collector.service.handler.sensor;
//
//import org.springframework.stereotype.Component;
//import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
//import ru.practicum.telemetry.collector.model.sensor.SensorEventType;
//import ru.practicum.telemetry.collector.model.sensor.mapper.SensorEventMapper;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Component
//public class SensorEventMapperRegistry {
//    private final Map<SensorEventType, SensorEventMapper<? extends SensorEvent>> mappers;
//
//    public SensorEventMapperRegistry(List<SensorEventMapper<? extends SensorEvent>> mapperList) {
//        this.mappers = mapperList.stream()
//                .collect(Collectors.toMap(SensorEventMapper::getType, Function.identity())
//                );
//    }
//
//    public <T extends SensorEvent> SensorEventMapper<T> get(SensorEventType type) {
//
//        return (SensorEventMapper<T>) mappers.get(type);
//    }
//}
