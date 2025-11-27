package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventProto.PayloadCase getProtoMessageType();

    void handle(SensorEventProto event);
}
