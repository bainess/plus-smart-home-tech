package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ConditionOperation;

public interface ConditionRepository extends JpaRepository<ConditionOperation, Long> {
}