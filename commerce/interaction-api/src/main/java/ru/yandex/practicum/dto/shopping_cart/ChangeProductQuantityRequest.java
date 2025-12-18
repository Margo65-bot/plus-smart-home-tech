package ru.yandex.practicum.dto.shopping_cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangeProductQuantityRequest {
    @NotBlank(message = "Id товара не может быть пустым")
    @Size(max = 50, message = "Id товара не должен иметь больше 50 символов")
    private String productId;

    @NotNull(message = "Количество товара не должно быть нулевым")
    @PositiveOrZero(message = "Количество товара должно быть неотрицательным числом")
    private Long newQuantity;
}
