package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(Producer<Void, SpecificRecordBase> producer) {
        super(producer);
    }

    @Override
    public DeviceRemovedEventAvro toAvro(HubEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(((DeviceRemovedEvent) event).getId())
                .build();
    }

    @Override
    public DeviceRemovedEventAvro toAvro(HubEventProto event) {
        DeviceRemovedEventProto deviceRemoved = event.getDeviceRemoved();
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemoved.getId())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public HubEventProto.PayloadCase getProtoMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}
