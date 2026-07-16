package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scenario_actions")
@IdClass(ScenarioActionId.class)
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioAction {

    @Id
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Id
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Id
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;
}
