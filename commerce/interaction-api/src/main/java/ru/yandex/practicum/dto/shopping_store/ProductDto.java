package ru.yandex.practicum.dto.shopping_store;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @Size(max = 50, message = "Id товара не должен иметь больше 50 символов")
    private String productId;

    @NotBlank(message = "Наименование товара не должно быть пустым")
    @Size(max = 50, message = "Наименование товара не должно иметь больше 50 символов")
    private String productName;

    @NotBlank(message = "Описание товара не должно быть пустым")
    @Size(max = 255, message = "Описание товара не должно иметь больше 255 символов")
    private String description;

    private String imageSrc;

    @NotNull(message = "Товар должен иметь статус остатка")
    private QuantityState quantityState;

    @NotNull(message = "Товар должен иметь статус")
    private ProductState productState;

    @NotNull(message = "Товар должен иметь категорию")
    private ProductCategory productCategory;

    @NotNull(message = "Товар должен иметь цену")
    @Positive(message = "Цена товара не должна быть отрицательной")
    @DecimalMin(value = "1.0", inclusive = false, message = "Цена товара не должна быть меньше 1.0")
    private BigDecimal price;
}
