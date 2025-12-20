package ru.yandex.practicum.dto.shopping_cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record ShoppingCartDto (
    @NotBlank(message = "Id корзины не может быть пустым")
    @Size(max = 50, message = "Id корзины не должен иметь больше 50 символов")
    String shoppingCartId,

    @NotNull(message = "Список id товаров не должен быть нулевым")
    Map<String, Long> products
) {}
