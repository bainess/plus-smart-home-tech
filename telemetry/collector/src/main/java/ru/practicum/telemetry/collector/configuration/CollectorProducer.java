package ru.practicum.telemetry.collector.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectorProducer {
    private static final String SENSOR_TOPIC = "telemetry.sensors.v1";
    private static final String HUB_TOPIC = "telemetry.hubs.v1";

    private final Producer<String, SpecificRecordBase> producer;

    public void sendSensorEvent(SensorEventAvro event) {
        if (event == null) {
            log.error("Cannot send null sensor event");
            return;
        }

        String hubId = event.getHubId();
        if (hubId == null || hubId.isEmpty()) {
            log.error("Sensor event has null or empty hubId: {}", event);
            return;
        }

        try {
            log.info("Sending sensor event to topic '{}': hubId='{}'",
                    SENSOR_TOPIC, hubId);

            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    SENSOR_TOPIC,
                    hubId,
                    event
            );

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send sensor event to Kafka", exception);
                } else {
                    log.info("Sensor event sent successfully: topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
            producer.flush();
        } catch (Exception e) {
            log.error("Failed to send sensor event to Kafka", e);
            throw new RuntimeException("Failed to send sensor event", e);
        }
    }

    public void sendHubEvent(HubEventAvro event) {
        if (event == null) {
            log.error("Cannot send null hub event");
            return;
        }

        String hubId = event.getHubId();
        if (hubId == null || hubId.isEmpty()) {
            log.error("Hub event has null or empty hubId: {}", event);
            return;
        }

        try {
            log.info("Sending hub event to topic '{}': hubId='{}'",
                    HUB_TOPIC, hubId);

            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    HUB_TOPIC,
                    hubId,
                    event
            );

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send hub event to Kafka", exception);
                } else {
                    log.info("Hub event sent successfully: topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
            producer.flush();
        } catch (Exception e) {
            log.error("Failed to send hub event to Kafka", e);
            throw new RuntimeException("Failed to send hub event", e);
        }
    }
}