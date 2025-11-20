package ru.yandex.practicum.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.serialize.deserializer.HubEventDeserializer;
import ru.yandex.practicum.serialize.deserializer.SensorsSnapshotDeserializer;

import java.util.List;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "smart-home-tech.kafka")
@Getter
@Setter
public class KafkaConsumerConfig {
    private String bootstrapServers;
    private String hubEventTopic;
    private String sensorSnapshotTopic;
    private String hubEventGroupId;
    private String snapshotGroupId;
    private String autoOffsetReset;

    @Bean
    public Consumer<Void, HubEventAvro> hubEventConsumer() {
        Properties config = setCommonProp();
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, hubEventGroupId);
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");

        Consumer<Void, HubEventAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(hubEventTopic));
        return consumer;
    }

    @Bean
    public Consumer<Void, SensorsSnapshotAvro> sensorsSnapshotConsumer() {
        Properties config = setCommonProp();
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorsSnapshotDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, snapshotGroupId);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "60000");
        config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        Consumer<Void, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(sensorSnapshotTopic));
        return consumer;
    }

    private Properties setCommonProp() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        return config;
    }

}
