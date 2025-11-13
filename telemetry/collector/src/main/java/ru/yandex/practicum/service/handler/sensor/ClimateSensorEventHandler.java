package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.ClimateSensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public ClimateSensorAvro toAvro(SensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(((ClimateSensorEvent) event).getCo2Level())
                .setHumidity(((ClimateSensorEvent) event).getHumidity())
                .setTemperatureC(((ClimateSensorEvent) event).getTemperatureC())
                .build();
    }

    @Override
    public ClimateSensorAvro toAvro(SensorEventProto event) {
        ClimateSensorProto climateSensorProto = event.getClimateSensor();
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(climateSensorProto.getCo2Level())
                .setHumidity(climateSensorProto.getHumidity())
                .setTemperatureC(climateSensorProto.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SensorEventProto.PayloadCase getProtoMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }
}
