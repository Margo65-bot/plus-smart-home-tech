package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.MotionSensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    public MotionSensorEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public MotionSensorAvro toAvro(SensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setMotion(((MotionSensorEvent) event).getMotion())
                .setVoltage(((MotionSensorEvent) event).getVoltage())
                .setLinkQuality(((MotionSensorEvent) event).getLinkQuality())
                .build();
    }

    @Override
    public MotionSensorAvro toAvro(SensorEventProto event) {
        MotionSensorProto motionSensorProto = event.getMotionSensor();
        return MotionSensorAvro.newBuilder()
                .setMotion(motionSensorProto.getMotion())
                .setVoltage(motionSensorProto.getVoltage())
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SensorEventProto.PayloadCase getProtoMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }
}
