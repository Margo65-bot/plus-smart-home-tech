package ru.yandex.practicum.data.service;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.data.entity.ScenarioAction;
import ru.yandex.practicum.data.entity.ScenarioCondition;
import ru.yandex.practicum.data.repository.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequestProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {
    private final ScenarioRepository scenarioRepository;

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub grpcClient;

    @Override
    @Transactional
    public void handle(SensorsSnapshotAvro sensorsSnapshotAvro) {
        log.debug("handle(SensorsSnapshotAvro sensorsSnapshotAvro) sensorsSnapshotAvro={}", sensorsSnapshotAvro);
        List<DeviceActionRequestProto> request = scenarioRepository.findByHubId(sensorsSnapshotAvro.getHubId()).stream()
                .filter(scenario -> scenario.getScenarioConditions().stream()
                        .allMatch(condition -> isConditionMet(condition, sensorsSnapshotAvro.getSensorsState())))
                .flatMap(scenario -> scenario.getScenarioActions().stream()
                        .map(action -> DeviceActionRequestProto.newBuilder()
                                .setHubId(sensorsSnapshotAvro.getHubId())
                                .setScenarioName(scenario.getName())
                                .setAction(buildActionProto(action))
                                .setTimestamp(buildTimestamp())
                                .build()))
                .toList();
        request.forEach(a -> grpcClient.handleDeviceAction(a));
    }

    private boolean isConditionMet(ScenarioCondition condition, Map<String, SensorStateAvro> sensorsState) {
        SensorStateAvro sensorState = sensorsState.get(condition.getSensor().getId());
        return sensorState != null &&
                sensorState.getData() != null &&
                ConditionMatcher.matches(condition.getCondition(), sensorState);
    }

    private DeviceActionProto buildActionProto(ScenarioAction action) {
        DeviceActionProto.Builder builder = DeviceActionProto.newBuilder()
                .setSensorId(action.getSensor().getId())
                .setType(Enum.valueOf(ActionTypeProto.class, action.getAction().getType().name()));

        Optional.ofNullable(action.getAction().getValue()).ifPresent(builder::setValue);
        return builder.build();
    }

    private Timestamp buildTimestamp() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }
}
