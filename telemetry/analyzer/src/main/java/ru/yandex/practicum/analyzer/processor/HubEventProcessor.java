    package ru.yandex.practicum.analyzer.processor;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.apache.avro.specific.SpecificRecordBase;
    import org.apache.kafka.clients.consumer.ConsumerRecord;
    import org.apache.kafka.clients.consumer.ConsumerRecords;
    import org.apache.kafka.clients.consumer.KafkaConsumer;
    import org.apache.kafka.common.errors.WakeupException;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.stereotype.Component;
    import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

    import java.time.Duration;
    import java.util.List;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class HubEventProcessor implements Runnable{
        private static final String HUB_TOPICS = "telemetry.hubs.v1";

        @Qualifier("hubConsumer")
        private final KafkaConsumer<String, SpecificRecordBase> consumer;

        @Override
        public void run() {
            try {
                consumer.subscribe(List.of(HUB_TOPICS));

                while (true) {
                    ConsumerRecords<String, SpecificRecordBase> records =
                            consumer.poll(Duration.ofMillis(500));

                    for (ConsumerRecord<String, SpecificRecordBase> record : records ) {
                        HubEventAvro event = (HubEventAvro) record.value();
                    }
                }
                // consumer.commitSync();
            } catch (WakeupException e) {

            } catch (Exception e) {
                log.info("Handling hub evenr error analyzer {}", e);
            } finally {
                try {
                    consumer.commitSync();
                } finally {
                    consumer.close();
                }
            }
        }
    }
