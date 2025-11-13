package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    SensorEventProto.PayloadCase getProtoMessageType();

    void handle(SensorEvent event);

    void handle(SensorEventProto event);
}
