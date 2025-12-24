package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    List<ShoppingCart> findByUsernameAndIsActiveOrderByIdDesc(String username, boolean isActive);

    Optional<ShoppingCart> findByUsernameAndIsActive(String username, boolean isActive);

    Optional<ShoppingCart> findByUsername(String username);
}
