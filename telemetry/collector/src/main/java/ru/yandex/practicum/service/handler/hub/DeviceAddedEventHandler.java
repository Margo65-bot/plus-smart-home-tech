package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    protected DeviceAddedEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public DeviceAddedEventAvro toAvro(HubEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(((DeviceAddedEvent) event).getId())
                .setType(handleEnum(((DeviceAddedEvent) event).getDeviceType(), DeviceTypeAvro.class))
                .build();
    }

    @Override
    public DeviceAddedEventAvro toAvro(HubEventProto event) {
        DeviceAddedEventProto deviceAdded = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(deviceAdded.getId())
                .setType(handleEnum(deviceAdded.getType(), DeviceTypeAvro.class))
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public HubEventProto.PayloadCase getProtoMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
