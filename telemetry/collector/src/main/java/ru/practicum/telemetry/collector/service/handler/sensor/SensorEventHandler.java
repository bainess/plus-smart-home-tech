package ru.practicum.telemetry.collector.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;


public interface SensorEventHandler {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event);

//    private final CollectorProducer producer;
//    private final SensorEventMapperRegistry registry;
//
//    public void handle(SensorEvent event) {
//        SensorEventMapper mapper = registry.get(event.getType());
//        if (mapper == null) {
//            throw new IllegalStateException("No mapper for type " + event.getType());
//        }
//        SensorEventAvro avro = mapper.toAvro(event);
//
//        log.info("INFO SENT TO KAFKA {}", avro);
//        producer.sendSensorEvent(avro);
//    }
}
