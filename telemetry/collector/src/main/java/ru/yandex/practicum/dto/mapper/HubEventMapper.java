package ru.yandex.practicum.dto.mapper;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.device.DeviceAction;
import ru.yandex.practicum.dto.hub.device.DeviceActionType;
import ru.yandex.practicum.dto.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.dto.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.dto.hub.device.DeviceType;
import ru.yandex.practicum.dto.hub.scenario.ScenarioAddedEvent;
import ru.yandex.practicum.dto.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.dto.hub.scenario.ScenarioConditionOperation;
import ru.yandex.practicum.dto.hub.scenario.ScenarioConditionType;
import ru.yandex.practicum.dto.hub.scenario.ScenarioRemovedEvent;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

public class HubEventMapper {
    public static SpecificRecordBase toAvro(HubEvent event) {
        SpecificRecordBase payload = switch (event) {
            case DeviceAddedEvent e -> toAvro(e);
            case DeviceRemovedEvent e -> toAvro(e);
            case ScenarioAddedEvent e -> toAvro(e);
            case ScenarioRemovedEvent e -> toAvro(e);
            default -> throw new IllegalArgumentException("Неподдерживаемый тип Hub Event: " + event.getClass().getName());
        };
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    public static DeviceAddedEventAvro toAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(toAvro(event.getDeviceType()))
                .build();
    }

    public static DeviceRemovedEventAvro toAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    public static ScenarioAddedEventAvro toAvro(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(event.getConditions().stream().map(HubEventMapper::toAvro).toList())
                .setActions(event.getActions().stream().map(HubEventMapper::toAvro).toList())
                .build();
    }

    public static ScenarioRemovedEventAvro toAvro(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    public static DeviceTypeAvro toAvro(DeviceType type) {
        return switch (type) {
            case DeviceType.CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case DeviceType.LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case DeviceType.MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case DeviceType.SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            case DeviceType.TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            default -> throw new IllegalArgumentException("Неподдерживаемый тип Device: " + type);
        };
    }

    public static ScenarioConditionAvro toAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(toAvro(condition.getType()))
                .setOperation(toAvro(condition.getOperation()))
                .setValue(condition.getValue())
                .build();
    }

    public static ConditionTypeAvro toAvro(ScenarioConditionType type) {
        return switch (type) {
            case ScenarioConditionType.MOTION -> ConditionTypeAvro.MOTION;
            case ScenarioConditionType.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ScenarioConditionType.SWITCH -> ConditionTypeAvro.SWITCH;
            case ScenarioConditionType.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ScenarioConditionType.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ScenarioConditionType.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            default -> throw new IllegalArgumentException("Неподдерживаемый тип Scenario Condition: " + type);
        };
    }

    public static ConditionOperationAvro toAvro(ScenarioConditionOperation operation) {
        return switch (operation) {
            case ScenarioConditionOperation.EQUALS -> ConditionOperationAvro.EQUALS;
            case ScenarioConditionOperation.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ScenarioConditionOperation.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            default -> throw new IllegalArgumentException("Неподдерживаемая операция Scenario Condition: " + operation);
        };
    }

    public static DeviceActionAvro toAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(toAvro(action.getType()))
                .setValue(action.getValue())
                .build();
    }

    public static ActionTypeAvro toAvro(DeviceActionType type) {
        return switch (type) {
            case DeviceActionType.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DeviceActionType.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case DeviceActionType.INVERSE -> ActionTypeAvro.INVERSE;
            case DeviceActionType.SET_VALUE -> ActionTypeAvro.SET_VALUE;
            default -> throw new IllegalArgumentException("Неподдерживаемый тип Scenario Condition: " + type);
        };
    }
}
