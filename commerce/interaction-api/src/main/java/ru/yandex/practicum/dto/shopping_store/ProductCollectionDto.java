package ru.yandex.practicum.dto.shopping_store;

import java.util.List;

public record ProductCollectionDto(
        List<ProductDto> content,
        List<SortDto> sort
) {}
