package service;

import dto.hub.HubEvent;
import dto.sensor.SensorEvent;

public interface EventService {
    void handleSensorEvent(SensorEvent sensorEvent);

    void handleHubEvent(HubEvent hubEvent);
}
