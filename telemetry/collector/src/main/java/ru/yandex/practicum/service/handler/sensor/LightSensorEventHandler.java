package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public LightSensorAvro toAvro(SensorEventProto event) {
        LightSensorProto lightSensorProto = event.getLightSensor();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorProto.getLinkQuality())
                .setLuminosity(lightSensorProto.getLuminosity())
                .build();
    }

    @Override
    public SensorEventProto.PayloadCase getProtoMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }
}
