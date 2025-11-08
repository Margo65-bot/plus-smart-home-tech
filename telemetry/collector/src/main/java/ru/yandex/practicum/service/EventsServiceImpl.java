package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.mapper.HubEventMapper;
import ru.yandex.practicum.dto.mapper.SensorEventMapper;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventService {
    private final Producer<Void, SpecificRecordBase> producer;

    @Value("${smart-home-tech.kafka.hub-event-topic}")
    private String hubEventTopicName;

    @Value("${smart-home-tech.kafka.sensor-event-topic}")
    private String sensorEventTopicName;

    @Override
    public void handleSensorEvent(SensorEvent sensorEvent) {
        SpecificRecordBase avroSensorEvent = SensorEventMapper.toAvro(sensorEvent);
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(sensorEventTopicName, avroSensorEvent);
        producer.send(record);
    }

    @Override
    public void handleHubEvent(HubEvent hubEvent) {
        SpecificRecordBase avroHubEvent = HubEventMapper.toAvro(hubEvent);
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(hubEventTopicName, avroHubEvent);
        producer.send(record);
    }
}
