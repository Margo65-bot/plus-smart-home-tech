package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record NewProductInWarehouseRequest (
    @NotBlank(message = "Id товара не должен быть пустым")
    String productId,

    Boolean fragile,

    @NotNull(message = "Размеры не должны быть нулевыми")
    DimensionDto dimension,

    @NotNull(message = "Вес не должен быть нулевым")
    @Positive(message = "Вес должен быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Вес не должен быть меньше 1.0")
    BigDecimal weight
    ) {}
