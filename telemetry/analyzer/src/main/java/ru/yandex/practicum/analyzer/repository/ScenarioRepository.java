package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ScenarioAddedEvent;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<ScenarioAddedEvent, Long> {
    List<ScenarioAddedEvent> findByHubId(String hubId);
    Optional<ScenarioAddedEvent> findByHubIdAndName(String hubId, String name);
}
