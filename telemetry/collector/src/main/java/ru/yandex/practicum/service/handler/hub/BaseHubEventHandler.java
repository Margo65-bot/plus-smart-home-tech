package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final Producer<Void, SpecificRecordBase> producer;

    @Value("${smart-home-tech.kafka.hub-event-topic}")
    private String hubEventTopicName;

    @Override
    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(this.getProtoMessageType())) {
            throw new IllegalArgumentException("Неподдерживаемый тип Hub Event Proto: " + event.getClass().getName());
        }
        HubEventAvro avroHubEvent = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(this.toAvro(event))
                .build();
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(hubEventTopicName, avroHubEvent);
        producer.send(record);
    }

    public abstract T toAvro(HubEventProto event);

    protected static <E extends Enum<E>, F extends Enum<F>> F handleEnum(E first, Class<F> secondClass) {
        return Enum.valueOf(secondClass, first.name());
    }
}
