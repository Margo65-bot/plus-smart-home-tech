package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DimensionDto (
    @NotNull(message = "Ширина не должна быть нулевой")
    @Positive(message = "Ширина должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Ширина не должна быть меньше 1.0")
    BigDecimal width,

    @NotNull(message = "Высота не должна быть нулевой")
    @Positive(message = "Высота должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Высота не должна быть меньше 1.0")
    BigDecimal height,

    @NotNull(message = "Толщина не должна быть нулевой")
    @Positive(message = "Толщина должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Толщина не должна быть меньше 1.0")
    BigDecimal depth
) {}
