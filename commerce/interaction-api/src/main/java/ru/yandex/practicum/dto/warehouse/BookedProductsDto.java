package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookedProductsDto {
    @NotNull(message = "Вес доставки не должен быть нулевым")
    private BigDecimal deliveryWeight;

    @NotNull(message = "Объем доставки не должен быть нулевым")
    private BigDecimal deliveryVolume;

    @NotNull(message = "Хрупкость не должна быть нулевой")
    private Boolean fragile;
}
