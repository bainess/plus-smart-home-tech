package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "scenarios"
)
@Builder
public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hub_id", nullable = false)
    private String hubId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "scenario",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<ScenarioAction> actions;

    @OneToMany(mappedBy = "scenario",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<ScenarioCondition> conditions;
}
