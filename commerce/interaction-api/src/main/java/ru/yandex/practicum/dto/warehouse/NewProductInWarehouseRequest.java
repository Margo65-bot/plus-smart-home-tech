package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {
    @NotBlank(message = "Id товара не должен быть пустым")
    private String productId;

    private Boolean fragile;

    @NotNull(message = "Размеры не должны быть нулевыми")
    private DimensionDto dimension;

    @NotNull(message = "Вес не должен быть нулевым")
    @Positive(message = "Вес должен быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Вес не должен быть меньше 1.0")
    private BigDecimal weight;
}
