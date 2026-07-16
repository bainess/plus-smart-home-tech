package ru.yandex.practicum.analyzer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class AnalyzerConfiguration {
    @Bean
    public KafkaConsumer<String, SpecificRecordBase> snapshotConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "snapshot_analyzer");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                SnapshotDeserializer.class);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        return new KafkaConsumer<>(config);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> hubConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "hub_analyzer");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                HubEventDeserializer.class);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        return new KafkaConsumer<>(config);
    }
}
