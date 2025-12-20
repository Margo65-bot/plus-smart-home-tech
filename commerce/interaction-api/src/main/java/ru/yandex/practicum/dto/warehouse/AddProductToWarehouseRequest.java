package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddProductToWarehouseRequest(
        String productId,

        @NotNull(message = "Количество не должно быть нулевым")
        @Positive(message = "Количество должно быть позитивным числом")
        Long quantity
) {
}
