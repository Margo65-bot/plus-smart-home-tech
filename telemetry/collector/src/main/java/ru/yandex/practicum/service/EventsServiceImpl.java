package ru.yandex.practicum.service;

import ru.yandex.practicum.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventsServiceImpl implements EventService {
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> protoHubEventHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> protoSensorEventHandlers;

    public EventsServiceImpl(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.protoHubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getProtoMessageType, Function.identity()));
        this.protoSensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getProtoMessageType, Function.identity()));
    }

    @Override
    public void handleSensorEvent(SensorEvent sensorEvent) {
        if (sensorEventHandlers.containsKey(sensorEvent.getType())) {
            sensorEventHandlers.get(sensorEvent.getType()).handle(sensorEvent);
        } else {
            throw new IllegalArgumentException("Обработчик для события " + sensorEvent.getType() + " не найден");
        }
    }

    @Override
    public void handleHubEvent(HubEvent hubEvent) {
        if (hubEventHandlers.containsKey(hubEvent.getType())) {
            hubEventHandlers.get(hubEvent.getType()).handle(hubEvent);
        } else {
            throw new IllegalArgumentException("Обработчик для события " + hubEvent.getType() + " не найден");
        }
    }

    @Override
    public void handleSensorEvent(SensorEventProto sensorEvent) {
        if (protoSensorEventHandlers.containsKey(sensorEvent.getPayloadCase())) {
            protoSensorEventHandlers.get(sensorEvent.getPayloadCase()).handle(sensorEvent);
        } else {
            throw new IllegalArgumentException("Обработчик для события " + sensorEvent.getPayloadCase() + " не найден");
        }
    }

    @Override
    public void handleHubEvent(HubEventProto hubEvent) {
        if (protoHubEventHandlers.containsKey(hubEvent.getPayloadCase())) {
            protoHubEventHandlers.get(hubEvent.getPayloadCase()).handle(hubEvent);
        } else {
            throw new IllegalArgumentException("Обработчик для события " + hubEvent.getPayloadCase() + " не найден");
        }
    }
}
