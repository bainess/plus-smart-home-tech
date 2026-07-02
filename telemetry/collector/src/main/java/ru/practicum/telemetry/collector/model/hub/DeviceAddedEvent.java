package ru.practicum.telemetry.collector.model.hub;

import lombok.*;

@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAddedEvent extends HubEvent{
    private String id;
    private DeviceType type;

    @Override
    public HubEventType getType() {return HubEventType.DEVICE_ADDED;}
}
