package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {
    HubEventType getMessageType();

    HubEventProto.PayloadCase getProtoMessageType();

    void handle(HubEvent event);

    void handle(HubEventProto event);
}
