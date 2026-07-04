package ru.practicum.telemetry.collector.controller;

import com.google.protobuf.util.JsonFormat;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorResponse;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Slf4j
@GrpcService
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<CollectorResponse> responseObserver) {
        try {
            log.info("Echo received");
            String json = JsonFormat.printer().preservingProtoFieldNames()
                    .print(request);
            log.info(json);
            responseObserver.onNext(CollectorResponse.newBuilder().setMessage(json).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}

