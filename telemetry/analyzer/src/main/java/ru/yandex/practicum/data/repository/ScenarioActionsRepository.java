package ru.yandex.practicum.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.data.entity.ScenarioAction;

public interface ScenarioActionsRepository extends JpaRepository<ScenarioAction, Long> {
}
