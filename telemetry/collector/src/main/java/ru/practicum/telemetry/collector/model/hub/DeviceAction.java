package ru.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
