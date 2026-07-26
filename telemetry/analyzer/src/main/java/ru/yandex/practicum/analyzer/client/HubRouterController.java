//package ru.yandex.practicum.analyzer.client;
//import com.google.protobuf.Empty;
//import io.grpc.stub.StreamObserver;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.devh.boot.grpc.server.service.GrpcService;
//import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
//import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
//
//
//
//@Slf4j
//@GrpcService
//@RequiredArgsConstructor
//public class HubRouterController
//        extends HubRouterControllerGrpc.HubRouterControllerImplBase {
//
//
//    @Override
//    public void handleDeviceAction(
//            DeviceActionRequest request,
//            StreamObserver<Empty> responseObserver) {
//
//
//        log.info(
//                "Получена команда от analyzer: {}",
//                request
//        );
//
//
//        try {
//
//            actionHandler.handle(request);
//
//
//            responseObserver.onNext(
//                    Empty.getDefaultInstance()
//            );
//
//            responseObserver.onCompleted();
//
//
//        } catch (Exception e) {
//
//            log.error(
//                    "Ошибка обработки команды",
//                    e
//            );
//
//            responseObserver.onError(
//                    e
//            );
//        }
//    }
//}