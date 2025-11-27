package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public ScenarioRemovedEventAvro toAvro(HubEventProto event) {
        ScenarioRemovedEventProto scenarioRemovedEvent = event.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getProtoMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }
}
