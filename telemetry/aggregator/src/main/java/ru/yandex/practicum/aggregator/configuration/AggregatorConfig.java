package ru.yandex.practicum.aggregator.configuration;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.configuration.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import telemetry.AvroSerializer;

import java.util.Properties;

@Slf4j
@Component
public class AggregatorConfig {
    @Bean
    public KafkaConsumer<String, SensorEventAvro> consumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                SensorEventDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");


        return new KafkaConsumer<>(config);
    }

    @Bean
    public KafkaProducer<String, SpecificRecordBase> producer() {

        Properties config = new Properties();

        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                AvroSerializer.class.getName());

        config.put(
                AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                "http://localhost:8081"
        );

        return new KafkaProducer<>(config);
    }
}
