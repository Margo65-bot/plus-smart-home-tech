package ru.yandex.practicum.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.data.service.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    private final SnapshotService snapshotService;
    private final Consumer<Void, SensorsSnapshotAvro> sensorsSnapshotConsumer;

    private volatile boolean running = true;

    public void start() {

        try {
            while (running) {
                ConsumerRecords<Void, SensorsSnapshotAvro> records = sensorsSnapshotConsumer.poll(Duration.ofMillis(500));

                if (!records.isEmpty()) {
                    processSnapshotBatch(records);
                }
            }

        } catch (WakeupException e) {
            log.info("Получен WakeupException - завершение работы SnapshotProcessor");
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшотов", e);
        } finally {
            sensorsSnapshotConsumer.close();
        }
    }

    private void processSnapshotBatch(ConsumerRecords<Void, SensorsSnapshotAvro> records) {
        try {
            for (ConsumerRecord<Void, SensorsSnapshotAvro> record : records) {
                snapshotService.handle(record.value());
            }

            sensorsSnapshotConsumer.commitSync();
            log.debug("Обработано {} снапшотов, оффсеты зафиксированы", records.count());

        } catch (Exception e) {
            log.error("Ошибка обработки снапшотов, оффсеты НЕ зафиксированы", e);
            throw e;
        }
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        sensorsSnapshotConsumer.wakeup();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
