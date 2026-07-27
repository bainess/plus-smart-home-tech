package ru.yandex.practicum.analyzer.client;



import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

import java.time.Instant;

@Slf4j
@Service
public class HubRouter {
    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterStub;

    public void handleDeviceAction(String hubId, String scenarioName, String sensorId, Action action, Instant timestamp) {
        ActionTypeProto protoType = ActionTypeProto.valueOf(action.getType().name());

        ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto.Builder actionBuilder =
                ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto.newBuilder()
                        .setType(protoType)
                        .setSensorId(sensorId);

        if (action.getValue() != null) {
            actionBuilder.setValue(action.getValue());
        }

        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setTimestamp(com.google.protobuf.Timestamp.newBuilder()
                        .setSeconds(timestamp.getEpochSecond())
                        .setNanos(timestamp.getNano())
                        .build())
                .setAction(actionBuilder.build())
                .build();

        sendAction(request);
    }

    public void sendAction(DeviceActionRequest request) {
        try {
            log.info("Отправка команды в Hub Router для хаба {} и устройства {}", request.getHubId(),
                    request.getScenarioName());
            hubRouterStub.handleDeviceAction(request);
        } catch (Exception e) {
            log.error(
                    "Ошибка отправки gRPC запроса в Hub Router",
                    e
            );
        }
    }
}