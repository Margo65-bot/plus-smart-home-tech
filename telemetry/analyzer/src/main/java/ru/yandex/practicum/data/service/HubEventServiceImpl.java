package ru.yandex.practicum.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.data.entity.Action;
import ru.yandex.practicum.data.entity.Condition;
import ru.yandex.practicum.data.entity.DeviceActionType;
import ru.yandex.practicum.data.entity.Scenario;
import ru.yandex.practicum.data.entity.ScenarioAction;
import ru.yandex.practicum.data.entity.ScenarioActionId;
import ru.yandex.practicum.data.entity.ScenarioCondition;
import ru.yandex.practicum.data.entity.ScenarioConditionId;
import ru.yandex.practicum.data.entity.ScenarioConditionOperation;
import ru.yandex.practicum.data.entity.ScenarioConditionType;
import ru.yandex.practicum.data.entity.Sensor;
import ru.yandex.practicum.data.repository.ActionRepository;
import ru.yandex.practicum.data.repository.ConditionRepository;
import ru.yandex.practicum.data.repository.ScenarioActionsRepository;
import ru.yandex.practicum.data.repository.ScenarioConditionRepository;
import ru.yandex.practicum.data.repository.ScenarioRepository;
import ru.yandex.practicum.data.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubEventServiceImpl implements HubEventService {
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionsRepository scenarioActionsRepository;

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        log.debug("handle(HubEventAvro event) event={}", event);
        if (event == null || event.getHubId() == null || event.getTimestamp() == null || event.getPayload() == null) {
            return;
        }
        switch (event.getPayload()) {
            case DeviceAddedEventAvro payload:
                addDevice(payload, event.getHubId());
                break;
            case DeviceRemovedEventAvro payload:
                removeDevice(payload, event.getHubId());
                break;
            case ScenarioAddedEventAvro payload:
                addScenario(payload, event.getHubId());
                break;
            case ScenarioRemovedEventAvro payload:
                removeScenario(payload, event.getHubId());
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип Hub Event: " + event.getPayload().getClass().getName());
        }
    }

    private void addScenario(ScenarioAddedEventAvro payload, String hubId) {
        if (payload == null || payload.getName() == null || payload.getConditions() == null || payload.getActions() == null) {
            return;
        }
        
        Scenario scenario = Scenario.builder()
                .hubId(hubId)
                .name(payload.getName())
                .build();
        scenarioRepository.save(scenario);

        List<Condition> conditions = payload.getConditions().stream()
                .map(c -> {
                    Integer value = switch (c.getValue()) {
                        case Boolean b -> b ? 1 : 0;
                        case Integer i -> i;
                        default -> null;
                    };
                    Condition condition = new Condition();
                    condition.setType(handleEnum(c.getType(), ScenarioConditionType.class));
                    condition.setOperation(handleEnum(c.getOperation(), ScenarioConditionOperation.class));
                    condition.setValue(value);
                    return condition;
                })
                .toList();
        conditions = conditionRepository.saveAll(conditions);

        List<ScenarioConditionAvro> avroConditions = payload.getConditions();
        Set<String> conditionSensorIds = avroConditions.stream()
                .map(ScenarioConditionAvro::getSensorId)
                .collect(Collectors.toSet());
        Map<String, Sensor> conditionSensors = sensorRepository.findAllByIdIn(conditionSensorIds)
                .stream()
                .collect(Collectors.toMap(Sensor::getId, Function.identity()));
        List<ScenarioCondition> scenarioConditions = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++) {
            ScenarioConditionAvro conditionAvro = avroConditions.get(i);
            Condition savedCondition = conditions.get(i);
            Sensor sensor = conditionSensors.get(conditionAvro.getSensorId());

            ScenarioConditionId scenarioConditionId = new ScenarioConditionId(
                    scenario.getId(),
                    sensor.getId(),
                    savedCondition.getId());

            ScenarioCondition scenarioCondition = new ScenarioCondition(
                    scenarioConditionId,
                    scenario,
                    sensor,
                    savedCondition);
            scenarioConditions.add(scenarioCondition);
        }

        scenarioConditionRepository.saveAll(scenarioConditions);

        List<Action> actions = payload.getActions().stream()
                .map(a -> {
                    Action action = new Action();
                    action.setType(handleEnum(a.getType(), DeviceActionType.class));
                    action.setValue(a.getValue());
                    return action;
                })
                .toList();

        actions = actionRepository.saveAll(actions);

        List<DeviceActionAvro> avroActions = payload.getActions();
        Set<String> actionSensorIds = avroActions.stream()
                .map(DeviceActionAvro::getSensorId)
                .collect(Collectors.toSet());
        Map<String, Sensor> actionSensors = sensorRepository.findAllByIdIn(actionSensorIds)
                .stream()
                .collect(Collectors.toMap(Sensor::getId, Function.identity()));
        List<ScenarioAction> scenarioActions = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++) {
            DeviceActionAvro actionAvro = avroActions.get(i);
            Action savedAction = actions.get(i);
            Sensor sensor = actionSensors.get(actionAvro.getSensorId());

            ScenarioActionId scenarioActionId = new ScenarioActionId(
                    scenario.getId(),
                    sensor.getId(),
                    savedAction.getId()
            );

            ScenarioAction scenarioAction = new ScenarioAction(
                    scenarioActionId,
                    scenario,
                    sensor,
                    savedAction
            );
            scenarioActions.add(scenarioAction);
        }
        scenarioActionsRepository.saveAll(scenarioActions);
    }

    private void removeScenario(ScenarioRemovedEventAvro payload, String hubId) {
        if (payload == null || payload.getName() == null) {
            return;
        }
        if (!scenarioRepository.existsByNameAndHubId(payload.getName(), hubId)) {
            return;
        }
        scenarioRepository.deleteByNameAndHubId(payload.getName(), hubId);
    }

    private void addDevice(DeviceAddedEventAvro payload, String hubId) {
        if (payload == null || payload.getId() == null || payload.getType() == null) {
            return;
        }
        if (sensorRepository.existsById(payload.getId())) {
            return;
        }
        Sensor sensor = new Sensor();
        sensor.setId(payload.getId());
        sensor.setHubId(hubId);
        sensorRepository.save(sensor);
    }

    private void removeDevice(DeviceRemovedEventAvro payload, String hubId) {
        if (payload == null || payload.getId() == null) {
            return;
        }
        if (!sensorRepository.existsById(payload.getId())) {
            return;
        }
        sensorRepository.deleteById(payload.getId());
    }

    private static <E extends Enum<E>, F extends Enum<F>> F handleEnum(E first, Class<F> secondClass) {
        return Enum.valueOf(secondClass, first.name());
    }
}
