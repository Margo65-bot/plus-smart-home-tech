package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface EventService {
    void handleSensorEvent(SensorEvent sensorEvent);

    void handleSensorEvent(SensorEventProto sensorEvent);

    void handleHubEvent(HubEvent hubEvent);

    void handleHubEvent(HubEventProto hubEvent);
}
