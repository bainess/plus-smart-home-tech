//package ru.practicum.telemetry.collector.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.practicum.telemetry.collector.model.hub.HubEvent;
//import ru.practicum.telemetry.collector.model.sensor.SensorEvent;
//import ru.practicum.telemetry.collector.service.handler.hub.HubEventHandler;
//import ru.practicum.telemetry.collector.service.handler.sensor.SensorEventHandler;
//
//@RestController
//@Slf4j
//@Validated
//@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
//@RequiredArgsConstructor
//public class EventController {
//    private final SensorEventHandler sensorEventHandler;
//    private final HubEventHandler hubEventHandler;
//
//    @PostMapping("/sensors")
//    public void collectSensorEvent(@Valid @RequestBody SensorEvent request) {
//        log.info("RECEIVED INFO {}", request);
//        sensorEventHandler.handle(request);
//    }
//
//    @PostMapping("/hubs")
//    public void collectHubEvents(@Valid @RequestBody HubEvent request) {
//        log.info("Received hub event {}", request);
//        hubEventHandler.handle(request);
//    }
//}
