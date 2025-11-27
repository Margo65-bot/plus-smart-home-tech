package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {
    public SwitchSensorEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public SwitchSensorAvro toAvro(SensorEventProto event) {
        SwitchSensorProto switchSensorProto = event.getSwitchSensor();
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getProtoMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }
}
