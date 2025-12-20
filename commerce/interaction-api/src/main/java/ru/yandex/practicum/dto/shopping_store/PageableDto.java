package ru.yandex.practicum.dto.shopping_store;

public record PageableDto(
        Integer page,
        Integer size,
        String sort
) {}

