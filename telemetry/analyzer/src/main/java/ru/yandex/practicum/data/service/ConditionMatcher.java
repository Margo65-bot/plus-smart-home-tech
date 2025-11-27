package ru.yandex.practicum.data.service;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.data.entity.Condition;
import ru.yandex.practicum.data.entity.ScenarioConditionOperation;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.Objects;

public class ConditionMatcher {
    public static boolean matches(Condition condition, SpecificRecordBase unknownData) {
        if (unknownData == null) return false;
        return switch (unknownData) {
            case ClimateSensorAvro data -> matches(condition, data);
            case LightSensorAvro data -> matches(condition, data);
            case MotionSensorAvro data -> matches(condition, data);
            case SwitchSensorAvro data -> matches(condition, data);
            case TemperatureSensorAvro data -> matches(condition, data);
            default -> false;
        };
    }

    private static boolean matches(Condition condition, ClimateSensorAvro data) {
        Object field = switch (condition.getType()) {
            case TEMPERATURE -> data.getTemperatureC();
            case CO2LEVEL -> data.getCo2Level();
            case HUMIDITY -> data.getHumidity();
            default -> null;
        };
        return matchesObject(field, condition.getValue(), condition.getOperation());
    }

    private static boolean matches(Condition condition, LightSensorAvro data) {
        Object field = switch (condition.getType()) {
            case LUMINOSITY -> data.getLuminosity();
            default -> null;
        };
        return matchesObject(field, condition.getValue(), condition.getOperation());
    }

    private static boolean matches(Condition condition, MotionSensorAvro data) {
        Object field = switch (condition.getType()) {
            case MOTION -> data.getMotion();
            default -> null;
        };
        return matchesObject(field, condition.getValue(), condition.getOperation());
    }

    private static boolean matches(Condition condition, SwitchSensorAvro data) {
        Object field = switch (condition.getType()) {
            case SWITCH -> data.getState();
            default -> null;
        };
        return matchesObject(field, condition.getValue(), condition.getOperation());
    }

    private static boolean matches(Condition condition, TemperatureSensorAvro data) {
        Object field = switch (condition.getType()) {
            case TEMPERATURE -> data.getTemperatureC();
            default -> null;
        };
        return matchesObject(field, condition.getValue(), condition.getOperation());
    }

    private static boolean matchesObject(Object field, Integer value, ScenarioConditionOperation operation) {
        if (field == null) return false;
        return switch (field) {
            case Integer i -> matchesInteger(i, value, operation);
            case Boolean b -> matchesBoolean(b, value, operation);
            default -> false;
        };
    }

    private static boolean matchesInteger(Integer v1, Integer v2, ScenarioConditionOperation operation) {
        return switch (operation) {
            case EQUALS -> Objects.equals(v1, v2);
            case GREATER_THAN -> v1 > v2;
            case LOWER_THAN -> v1 < v2;
            default -> false;
        };
    }

    private static boolean matchesBoolean(Boolean b1, Integer v2, ScenarioConditionOperation operation) {
        Boolean b2 = v2 != null && v2 > 0;
        return switch (operation) {
            case EQUALS -> b1.equals(b2);
            default -> false;
        };
    }
}
