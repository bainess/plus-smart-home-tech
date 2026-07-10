package ru.yandex.practicum.aggregator.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.service.SensorsSnapShotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private static final String SENSOR_TOPICS = "telemetry.sensors.v1";
    private static final String SNAPSHOT_TOPIC = "telemetry.snapshots.v1";

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final SensorsSnapShotService snapShotService;

    public void start() {
        try {
            consumer.subscribe(List.of(SENSOR_TOPICS));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();

                    snapShotService.updateSensorSnapShot(event)
                            .ifPresent(this::sendSnapshot);

                    log.info(
                            "Received: topic={}, partition={}, offset={}, key={}, value={}",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()
                    );
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Handling event error", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                consumer.close();
                log.info("Consumer closed");
                producer.close();
                log.info("Producer closed");
            }
        }
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(
                        SNAPSHOT_TOPIC,
                        snapshot.getHubId(),
                        snapshot
                );

        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Sending snapshot error", exception);
                return;
            }
            log.info("Snapshot sent in partition: {}, offset: {}", metadata.partition(), metadata.offset());

        });
    }
}
