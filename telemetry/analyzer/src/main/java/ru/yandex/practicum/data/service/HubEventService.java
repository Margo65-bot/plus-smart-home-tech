package ru.yandex.practicum.data.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventService {
    void handle(HubEventAvro event);
}
