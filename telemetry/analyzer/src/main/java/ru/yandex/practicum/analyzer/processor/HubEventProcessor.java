package ru.yandex.practicum.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component

public class HubEventProcessor implements Runnable {
    private static final String HUB_TOPICS = "telemetry.hubs.v1";
    @Qualifier("hubConsumer")
    private final KafkaConsumer<String, SpecificRecordBase> consumer;
    private final HubEventService hubEventService;

    public HubEventProcessor(
            @Qualifier("hubConsumer")
            KafkaConsumer<String, SpecificRecordBase> consumer, HubEventService hubEventService) {
        this.consumer = consumer;
        this.hubEventService = hubEventService;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(HUB_TOPICS));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records =
                        consumer.poll(Duration.ofMillis(500));

                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    HubEventAvro event = (HubEventAvro) record.value();
                    Object payload = event.getPayload();
                    if (payload instanceof DeviceAddedEventAvro deviceAdded) {
                        log.info("Sensor Received to add- {}", deviceAdded);
                        hubEventService.addSensor(event);
                    } else if (payload instanceof DeviceRemovedEventAvro deviceRemoved) {
                        log.info("Sensor Received to remove- {}", deviceRemoved);
                        hubEventService.removeSensor(event);
                    } else if (payload instanceof ScenarioAddedEventAvro scenarioAdded) {
                        log.info("Scenario Received to add - {}", scenarioAdded);
                        hubEventService.addScenario(event);
                    } else if (payload instanceof ScenarioRemovedEventAvro scenarioRemoved) {
                        log.info("Scenario Received to remove - {}", scenarioRemoved);
                        hubEventService.removeScenario(event);
                    }
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {

        } catch (Exception e) {
            log.info("Handling hub event error analyzer {}", e.getMessage(), e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
