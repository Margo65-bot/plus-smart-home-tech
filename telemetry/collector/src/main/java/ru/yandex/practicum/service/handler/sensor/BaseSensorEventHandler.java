package ru.yandex.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    private final Producer<Void, SpecificRecordBase> producer;

    @Value("${smart-home-tech.kafka.sensor-event-topic}")
    private String sensorEventTopicName;

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(this.getProtoMessageType())) {
            throw new IllegalArgumentException("Неподдерживаемый тип Sensor Event Proto: " + event.getClass().getName());
        }
        SensorEventAvro avroSensorEvent = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(this.toAvro(event))
                .build();
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(sensorEventTopicName, avroSensorEvent);
        producer.send(record);
    }

    public abstract T toAvro(SensorEventProto event);
}
