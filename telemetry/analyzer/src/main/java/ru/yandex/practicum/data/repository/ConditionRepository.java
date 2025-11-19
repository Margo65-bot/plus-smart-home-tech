package ru.yandex.practicum.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.data.entity.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
