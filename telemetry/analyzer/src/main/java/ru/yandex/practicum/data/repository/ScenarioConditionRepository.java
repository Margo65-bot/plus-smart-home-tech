package ru.yandex.practicum.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.data.entity.ScenarioCondition;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, Long> {
}
