package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
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
public class DimensionDto {
    @NotNull(message = "Ширина не должна быть нулевой")
    @Positive(message = "Ширина должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Ширина не должна быть меньше 1.0")
    private BigDecimal width;

    @NotNull(message = "Высота не должна быть нулевой")
    @Positive(message = "Высота должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Высота не должна быть меньше 1.0")
    private BigDecimal height;

    @NotNull(message = "Толщина не должна быть нулевой")
    @Positive(message = "Толщина должна быть позитивным числом")
    @DecimalMin(value = "1.0", message = "Толщина не должна быть меньше 1.0")
    private BigDecimal depth;
}
