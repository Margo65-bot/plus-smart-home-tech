package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public ScenarioAddedEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent =  event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(scenarioAddedEvent.getConditionList().stream().map(ScenarioAddedEventHandler::toAvro).toList())
                .setActions(scenarioAddedEvent.getActionList().stream().map(ScenarioAddedEventHandler::toAvro).toList())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getProtoMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private static ScenarioConditionAvro toAvro(ScenarioConditionProto condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(handleEnum(condition.getType(), ConditionTypeAvro.class))
                .setOperation(handleEnum(condition.getOperation(), ConditionOperationAvro.class))
                .setValue(
                        switch (condition.getType()) {
                            case MOTION, SWITCH ->  condition.getBoolValue();
                            default -> condition.getIntValue();
                })
                .build();
    }

    private static DeviceActionAvro toAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(handleEnum(action.getType(), ActionTypeAvro.class))
                .setValue(action.getValue())
                .build();
    }
}
