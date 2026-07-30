package ru.yandex.practicum.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.ScenarioService;
import ru.yandex.practicum.analyzer.service.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {
    private static final String SNAPSHOT_TOPICS = "telemetry.snapshots.v1";
    private final KafkaConsumer<String, SpecificRecordBase> consumer;
    private final ScenarioService scenarioService;
    private final SnapshotService snapshotService;

    public SnapshotProcessor(@Qualifier("snapshotConsumer")
                             KafkaConsumer<String, SpecificRecordBase> consumer,
                             ScenarioService scenarioService,
                             SnapshotService snapshotService) {
        this.consumer = consumer;
        this.scenarioService = scenarioService;
        this.snapshotService = snapshotService;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(SNAPSHOT_TOPICS));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records =
                        consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("Record received {}", record);
                    SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();
                    log.info("Snapshot received {}", snapshot);

                    snapshotService.updateSnapshot(snapshot);
                    scenarioService.applyScenario(snapshot.getHubId());

                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {

        } catch (Exception e) {
            log.info("Handling event error in analyzer {}", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
