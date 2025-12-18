package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWarehouseRequest {
    private String productId;

    @NotNull(message = "Количество не должно быть нулевым")
    @Positive(message = "Количество должно быть позитивным числом")
    private Long quantity;
}
