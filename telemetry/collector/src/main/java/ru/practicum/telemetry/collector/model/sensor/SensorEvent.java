package ru.practicum.telemetry.collector.model.sensor;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
      //  defaultImpl = SensorEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR"),
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR"),
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_SENSOR")}
)
@Getter
@Setter
@ToString
public abstract class SensorEvent {

    private String id;
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
