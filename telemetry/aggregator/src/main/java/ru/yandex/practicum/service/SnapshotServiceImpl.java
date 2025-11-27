package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {
    private final Producer<Void, SpecificRecordBase> producer;

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Value("${smart-home-tech.kafka.sensor-snapshot-topic}")
    private String sensorSnapshotTopicName;

    @Override
    public void handle(SensorEventAvro sensorEventAvro) {
        log.debug("handle(): sensorEventAvro={}", sensorEventAvro);
        if (sensorEventAvro == null || sensorEventAvro.getId() == null || sensorEventAvro.getHubId() == null ||
                sensorEventAvro.getPayload() == null || sensorEventAvro.getTimestamp() == null) {
            return;
        }

        SensorsSnapshotAvro snapshot = snapshots.get(sensorEventAvro.getHubId());
        SensorStateAvro sensorState = null;
        if (snapshot != null) {
            sensorState = snapshot.getSensorsState().get(sensorEventAvro.getId());
        }

        if (sensorState != null && Objects.equals(sensorState.getData(), sensorEventAvro.getPayload())) {
            return;
        }

        if (sensorState != null && sensorState.getTimestamp() != null && sensorState.getTimestamp().isAfter(sensorEventAvro.getTimestamp())) {
            return;
        }

        SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                .setTimestamp(sensorEventAvro.getTimestamp())
                .setData(sensorEventAvro.getPayload())
                .build();

        if (snapshot == null) {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(sensorEventAvro.getHubId())
                    .setTimestamp(sensorEventAvro.getTimestamp())
                    .setSensorsState(new HashMap<>())
                    .build();
        } else {
            snapshot = SensorsSnapshotAvro.newBuilder(snapshot)
                    .setHubId(sensorEventAvro.getHubId())
                    .setTimestamp(sensorEventAvro.getTimestamp())
                    .build();
        }

        snapshot.getSensorsState().put(sensorEventAvro.getId(), newSensorState);
        snapshots.put(sensorEventAvro.getHubId(), snapshot);

        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(sensorSnapshotTopicName, snapshot);
        producer.send(record);
    }
}
