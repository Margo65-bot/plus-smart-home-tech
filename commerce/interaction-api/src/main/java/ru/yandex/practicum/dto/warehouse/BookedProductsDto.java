package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BookedProductsDto (
    @NotNull(message = "Вес доставки не должен быть нулевым")
    BigDecimal deliveryWeight,

    @NotNull(message = "Объем доставки не должен быть нулевым")
    BigDecimal deliveryVolume,

    @NotNull(message = "Хрупкость не должна быть нулевой")
    Boolean fragile
    ) {}
