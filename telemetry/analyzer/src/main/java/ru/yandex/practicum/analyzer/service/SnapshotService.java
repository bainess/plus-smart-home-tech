package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SnapshotService {
//    @GrpcClient("hub-router")
//    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;
    private final Map<String, SensorsSnapshotAvro> snapshots;

    public SnapshotService() {
        this.snapshots = new HashMap<>();
    }

    public SensorsSnapshotAvro updateSnapshot(SensorsSnapshotAvro snapshot) {
        snapshots.put(snapshot.getHubId(), snapshot);
        log.info("Snapshot {} updated {}", snapshot.getHubId(), snapshot);
        return snapshot;
    }

    public SensorsSnapshotAvro getSnapshot(String hubId) {
        return snapshots.get(hubId);
    }


}
