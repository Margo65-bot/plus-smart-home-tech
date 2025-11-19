package ru.yandex.practicum.data.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotService {
    void handle(SensorsSnapshotAvro snapshot);
}
