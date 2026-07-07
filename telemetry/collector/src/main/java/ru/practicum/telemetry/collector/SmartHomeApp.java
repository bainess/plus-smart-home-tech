package ru.practicum.telemetry.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.practicum.telemetry")
public class SmartHomeApp {
    public static void main(String[] args) {
        SpringApplication.run(SmartHomeApp.class, args);
    }
}
