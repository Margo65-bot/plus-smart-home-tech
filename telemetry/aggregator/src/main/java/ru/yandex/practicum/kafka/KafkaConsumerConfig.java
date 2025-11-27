package ru.yandex.practicum.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.serialize.deserializer.SensorEventDeserializer;

import java.util.List;
import java.util.Properties;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public Consumer<Void, SensorEventAvro> sensorEventConsumer(
            @Value("${smart-home-tech.kafka.bootstrap-servers}") String kafkaBootstrapServers,
            @Value("${smart-home-tech.kafka.sensor-event-topic}") String sensorEventTopic,
            @Value("${smart-home-tech.kafka.group-id}") String groupId,
            @Value("${smart-home-tech.kafka.auto-offset-reset}") String autoOffsetReset,
            @Value("${smart-home-tech.kafka.enable-auto-commit}") boolean enableAutoCommit
    ) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);

        Consumer<Void, SensorEventAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(sensorEventTopic));
        return consumer;
    }
}
