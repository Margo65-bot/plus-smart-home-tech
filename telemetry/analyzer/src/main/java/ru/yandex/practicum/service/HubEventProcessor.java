package ru.yandex.practicum.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.data.service.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final HubEventService hubEventService;
    private final Consumer<Void, HubEventAvro> hubEventConsumer;

    private volatile boolean running = true;

    @Override
    public void run() {
        try {
            while (running) {
                ConsumerRecords<Void, HubEventAvro> records = hubEventConsumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<Void, HubEventAvro> record : records) {
                    hubEventService.handle(record.value());
                }

                if (!records.isEmpty()) {
                    log.debug("Обработано {} событий хаба", records.count());
                }
            }

        } catch (WakeupException e) {
            log.info("Получен WakeupException - завершение работы HubEventProcessor");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий хаба", e);
        } finally {
            hubEventConsumer.close();
        }
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        hubEventConsumer.wakeup();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
