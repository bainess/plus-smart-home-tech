package ru.yandex.practicum.analyzer.model;

import lombok.*;


@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {return HubEventType.DEVICE_ADDED;}
}
