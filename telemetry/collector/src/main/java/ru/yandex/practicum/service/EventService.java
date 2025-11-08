package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

public interface EventService {
    void handleSensorEvent(SensorEvent sensorEvent);

    void handleHubEvent(HubEvent hubEvent);
}
