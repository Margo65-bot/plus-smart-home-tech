package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SnapshotService {
    void handle(SensorEventAvro sensorEventAvro);
}
