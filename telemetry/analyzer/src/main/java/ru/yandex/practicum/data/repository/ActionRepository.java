package ru.yandex.practicum.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.data.entity.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
