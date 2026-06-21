package ru.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HubEvent {
    private HubEventType type;
}
