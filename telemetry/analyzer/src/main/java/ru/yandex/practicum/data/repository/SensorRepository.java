package ru.yandex.practicum.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.data.entity.Sensor;

import java.util.Collection;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    List<Sensor> findAllByIdIn(Collection<String> ids);
}
