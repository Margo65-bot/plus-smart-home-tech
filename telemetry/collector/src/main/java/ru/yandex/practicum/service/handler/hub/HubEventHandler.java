package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {

    HubEventProto.PayloadCase getProtoMessageType();

    void handle(HubEventProto event);
}
