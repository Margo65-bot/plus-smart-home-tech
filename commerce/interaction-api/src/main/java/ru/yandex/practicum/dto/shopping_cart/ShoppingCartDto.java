package ru.yandex.practicum.dto.shopping_cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class ShoppingCartDto {
    @NotBlank(message = "Id корзины не может быть пустым")
    @Size(max = 50, message = "Id корзины не должен иметь больше 50 символов")
    private String shoppingCartId;

    @NotNull(message = "Список id товаров не должен быть нулевым")
    private Map<String, Long> products;
}
