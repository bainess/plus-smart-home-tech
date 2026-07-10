package ru.yandex.practicum.analyzer.model;

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
