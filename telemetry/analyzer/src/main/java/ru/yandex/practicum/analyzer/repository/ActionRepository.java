package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ActionType;

public interface ActionRepository extends JpaRepository<ActionType, Long> {
}